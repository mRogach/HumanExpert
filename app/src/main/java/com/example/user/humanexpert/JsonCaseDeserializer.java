package com.example.user.humanexpert;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by RICHI on 2014.10.19..
 */
public class JsonCaseDeserializer implements JsonDeserializer<CaseClass> {

    @Override
    public CaseClass deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject caseObject = (JsonObject) jsonObject.get("case");
        CaseClass cs = null;
        String text = "";
        if (caseObject.has("text")) {
            text = caseObject.get("text").getAsString();
        }

        int id = 0;
        if (caseObject.has("id")) {
            id = caseObject.get("id").getAsInt();
        }

        String url = "";
        if (caseObject.has("image")) {
            JsonElement jsonElement = caseObject.get("image");
            url = jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
        }
        if (caseObject.has("answers")) {
            JsonObject pozitiveJson = (JsonObject) caseObject.getAsJsonArray("answers").get(0);
            Answer answerYes = new Answer();

            String textAnswerP = pozitiveJson.get("text").getAsString();
            int idAnswerP = pozitiveJson.get("id").getAsInt();
            int caseIdP = pozitiveJson.get("caseId").getAsInt();
            answerYes.setNewText(textAnswerP);
            answerYes.setNewId(idAnswerP);
            answerYes.setNewCaseId(caseIdP);
            JsonObject negativeJson = (JsonObject) caseObject.getAsJsonArray("answers").get(1);
            Answer answerNo = new Answer();
            String textAnswer = negativeJson.get("text").getAsString();
            int idAnswer = negativeJson.get("id").getAsInt();
            int caseId = negativeJson.get("caseId").getAsInt();
            answerYes.setNewText(textAnswer);
            answerYes.setNewId(idAnswer);
            answerYes.setNewCaseId(caseId);


            cs = new CaseClass();
            cs.setText(text);
            cs.setImageUrl(url);
            cs.setId(id);
            cs.setPositive(answerYes);
            cs.setNegative(answerNo);
        }
            return cs;

        }
    }
