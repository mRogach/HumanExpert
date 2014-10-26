package com.example.user.humanexpert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by User on 19.10.2014.
 */
public class DBController {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    DBController(Context context){
        this.context = context;
    }


    public long insertCase(CaseClass caseClass) {
        if (!database.isOpen()) {
            open();
        }

        String query = "SELECT  * FROM " + dbHelper.TABLE_CASE + " WHERE " + dbHelper.KEY_CASE_ID + " = " + caseClass.getId();
        Cursor c = database.rawQuery(query, null);
        int count = c.getCount();
        c.close();

        ContentValues values = new ContentValues();

        values.put(dbHelper.KEY_CASE_ID, caseClass.getId());
        values.put(dbHelper.KEY_CASE_TEXT, caseClass.getText());
        values.put(dbHelper.KEY_CASE_IMAGE, caseClass.getImageUrl());
        values.put(dbHelper.KEY_CASE_ANSWER_YES_ID, caseClass.getPositive().getNewId());
        values.put(dbHelper.KEY_CASE_ANSWER_NO_ID, caseClass.getNegative().getNewId());
        values.put(dbHelper.KEY_CASE_ANSWER_YES_TEXT, caseClass.getPositive().getNewText());
        values.put(dbHelper.KEY_CASE_ANSWER_NO_TEXT, caseClass.getNegative().getNewText());
        long row_id;

        if (count == 0) {
            row_id = database.insert(dbHelper.TABLE_CASE, null, values);
        } else {
            row_id = database.update(DBHelper.TABLE_CASE,
                    values,
                    DBHelper.KEY_CASE_ID  + " = ?",
                    new String[] {String.valueOf(caseClass.getId())});
        }
        database.close();
        return row_id;
    }




    public long insertScenarioList(ArrayList<Scenario> scenarios) {
        if (!database.isOpen()) {
            open();
        }
        String query = "SELECT  * FROM " + dbHelper.TABLE_SCEN;
        Cursor c = database.rawQuery(query, null);
        int count = c.getCount();
        c.close();
        long row_id = 0;
        ContentValues values = new ContentValues();
        if (count == 0) {
            for (int i = 0; i < scenarios.size(); i++) {
                values.put(dbHelper.KEY_SCEN_ID, scenarios.get(i).getId());
                values.put(dbHelper.KEY_SCEN_TEXT, scenarios.get(i).getProblemTitle());
                values.put(dbHelper.KEY_SCEN_CASEID, scenarios.get(i).getCaseId());
                row_id = database.insert(dbHelper.TABLE_SCEN, null, values);
            }
        } else {
            for (int i = 0; i < scenarios.size(); i++) {
                row_id = database.update(DBHelper.TABLE_SCEN,
                        values,
                        DBHelper.KEY_SCEN_ID + " = ?",
                        new String[]{String.valueOf(scenarios.get(i).getId())});
            }
        }
        database.close();
        return row_id;
    }



    public CaseClass getCaseClass(long case_id) {
        if (!database.isOpen()) {
            open();
        }
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_CASE + " WHERE " + dbHelper.KEY_CASE_ID + " = " + case_id;
        Log.e(dbHelper.LOG, selectQuery);
        Cursor c = database.rawQuery(selectQuery, null);
        int count = c.getCount();
        if (c != null || count != 0) {
            c.moveToFirst();
        } else {
            return null;
        }
        CaseClass caseClass = new CaseClass();
        caseClass.setId(c.getInt(c.getColumnIndex(dbHelper.KEY_CASE_ID)));
        caseClass.setText((c.getString(c.getColumnIndex(dbHelper.KEY_CASE_TEXT))));
        caseClass.setImageUrl(c.getString(c.getColumnIndex(dbHelper.KEY_CASE_IMAGE)));
        Answer answerP = new Answer();
        answerP.setNewId(c.getInt(c.getColumnIndex(dbHelper.KEY_CASE_ANSWER_YES_ID)));
        caseClass.setPositive(answerP);
        Answer answerN = new Answer();
        answerN.setNewId(c.getInt(c.getColumnIndex(dbHelper.KEY_CASE_ANSWER_NO_ID)));
        caseClass.setNegative(answerN);

        database.close();

        return caseClass;
    }



    public ArrayList<Scenario> getAllScenarios() {
        if (!database.isOpen()) {
            open();
        }
        ArrayList<Scenario> scenariosList = new ArrayList<Scenario>();
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_SCEN;
        Log.e(dbHelper.LOG, selectQuery);

        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Scenario scenario = new Scenario();
                scenario.setId(c.getInt(c.getColumnIndex(dbHelper.KEY_SCEN_ID)));
                scenario.setProblemTitle((c.getString(c.getColumnIndex(dbHelper.KEY_SCEN_TEXT))));
                scenario.setCaseId(c.getInt(c.getColumnIndex(dbHelper.KEY_SCEN_CASEID)));
                scenariosList.add(scenario);
            } while (c.moveToNext());
        }
        database.close();

        return scenariosList;
    }


    public DBController open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
}
