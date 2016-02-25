package com.jbirdvegas.echo.common;

class AnswerModel {
    public String error
    public String meal
    public String menu
    public String school

    public String normalizeForVerbalResponse() {
        "For $meal Wilson county $school schools will be serving ${menu.replaceAll("\\P{Print}", "")}"
    }
}