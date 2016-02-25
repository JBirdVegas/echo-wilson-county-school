package com.jbirdvegas.echo;

import com.google.gson.Gson;
import com.jbirdvegas.echo.common.AnswerModel;
import com.jbirdvegas.echo.common.QuestionModel;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuGetter {
    public static Logger sLogger = Logger.getLogger(MenuGetter.class);

    public static AnswerModel doPost(QuestionModel question) {
        // TODO: pretty url? meh it's just a backend uri though it might be best applied to aws static url
        // TODO: url context infers versioning dependency on ec2 module :( (fix is in ec2/tomcat setup, bitnami pains)
        String baseUrl = "http://ec2-54-88-151-2.compute-1.amazonaws.com/ec2-1.1-SNAPSHOT/v1/rest/" + question.meal;
        Gson gson = new Gson();

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(baseUrl).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            connection.getOutputStream().write(gson.toJson(question).getBytes());
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String response = IOUtils.toString(connection.getInputStream());
                sLogger.debug("Response from server: " + response);
                return gson.fromJson(response, AnswerModel.class);
            } else {
                String error = IOUtils.toString(connection.getErrorStream());
                AnswerModel answerError = new AnswerModel();
                answerError.error = "status {" + responseCode + "} error {" + error + "}";
                return answerError;
            }
        } catch (IOException e) {
            e.printStackTrace();
            AnswerModel answerModel = new AnswerModel();
            answerModel.error = "Failed to communicate with backend: " + e.getMessage();
            return answerModel;
        }
    }
}