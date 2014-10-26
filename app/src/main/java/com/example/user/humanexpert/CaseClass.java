package com.example.user.humanexpert;

/**
 * Created by User on 17.10.2014.
 */
public class CaseClass {
    private int id;
    private String text;
    private String imageUrl;
//    private ArrayList<Answer> list;
    private Answer negative;
    private Answer positive;

    public Answer getNegative() {
        return negative;
    }

    public void setNegative(Answer negative) {
        this.negative = negative;
    }

    public Answer getPositive() {
        return positive;
    }

    public void setPositive(Answer positive) {
        this.positive = positive;
    }

    CaseClass(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
