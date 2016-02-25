package com.jbirdvegas.echo.common

import java.text.SimpleDateFormat;

/**
 * Default question.  Each with a default value.  These values can be overridden for the question by verbal input.
 */
public class QuestionModel {
    // assume user wants to know about lunch
    public String meal = 'Lunch'
    // assume elementary because... my kids
    public String when = 'Elementary'
    // assume today
    public String school = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
}
