package com.jbirdvegas.echo

import com.google.gson.GsonBuilder
import com.jbirdvegas.echo.common.AnswerModel
import com.jbirdvegas.echo.common.QuestionModel

class MenuGetter {
    def mGson = new GsonBuilder().create()

    public static AnswerModel doPost(QuestionModel question) {
        // TODO: pretty url? meh it's just a backend uri though it might be best applied to aws static url
        // TODO: url context infers versioning dependency on ec2 module :( (fix is in ec2/tomcat setup, bitnami pains)
        def baseUrl = "http://ec2-54-88-151-2.compute-1.amazonaws.com/ec2-1.1-SNAPSHOT/v1/rest/$question.meal"

        HttpURLConnection connection = baseUrl.toURL().openConnection() as HttpURLConnection
        connection.setDoOutput(true)
        connection.outputStream.withWriter { Writer writer ->
            writer << mGson.toJson(question)
        }
        if (connection.responseCode == 200) {
            // success
            return connection.inputStream.withReader { Reader reader -> mGson.fromJson(reader.text, AnswerModel) }
        } else {
            def fail = new AnswerModel()
            fail.error = "Response was not success {$connection.responseCode} ${connection.errorStream.withReader { reader -> reader }}}"
            return fail
        }
    }
}