package com.jbirdvegas.echo

import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MenuGetter {
    Logger mLogger = LoggerFactory.getLogger(MenuGetter.class)

    public static String doPost(String meal = 'Lunch', String school, String when) {
        def baseUrl = "http://ec2-54-88-151-2.compute-1.amazonaws.com/ec2-1.1-SNAPSHOT/v1/rest/$meal"

        PostRequestModel request = new PostRequestModel()
        request.when = when
        request.school = school

        HttpURLConnection connection = baseUrl.toURL().openConnection() as HttpURLConnection
        connection.setDoOutput(true)
        connection.outputStream.withWriter { Writer writer ->
            writer << new Gson().toJson(request)
        }
        if (connection.responseCode == 200) {
            // success
            return connection.inputStream.withReader { Reader reader -> reader.text }
        } else return {
            return "{\"error\":\"responseCode $connection.responseCode\"}"
        }
    }
}
