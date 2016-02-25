package com.jbirdvegas.echo.common;

public class AnswerModel {
    public AnswerModel() {
        // do nothing...
    }

    public String error;
    public String meal;
    public String menu;
    public String school;

    public String normalizeForVerbalResponse() {
        return error == null
                ? String.format("For %s Wilson county %s schools will be serving %s", meal, school, menu)
                : "We had a problem, " + error;
    }
}