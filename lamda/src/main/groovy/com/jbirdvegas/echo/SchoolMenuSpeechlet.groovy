package com.jbirdvegas.echo

import com.amazon.speech.slu.Intent
import com.amazon.speech.speechlet.IntentRequest
import com.amazon.speech.speechlet.LaunchRequest
import com.amazon.speech.speechlet.Session
import com.amazon.speech.speechlet.SessionEndedRequest
import com.amazon.speech.speechlet.SessionStartedRequest
import com.amazon.speech.speechlet.Speechlet
import com.amazon.speech.speechlet.SpeechletException
import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.PlainTextOutputSpeech
import com.amazon.speech.ui.SimpleCard
import com.google.gson.Gson
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import java.text.ParseException
import java.text.SimpleDateFormat

public class SchoolMenuSpeechlet implements Speechlet {
    // Wed Feb 24 00:00:00 UTC 2016

    public SchoolMenuSpeechlet() {
        println "School Menu Speechlet"
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : "";
        println "Discovered intent named: $intentName"
        switch (intentName) {
            case "AMAZON.HelpIntent":
                return getHelpResponse();
            case "SchoolMenuIntent":
                return getSchoolMenuIntent(intent);
        }
        return null;
    }

    public SpeechletResponse getSchoolMenuIntent(Intent intent) {
        String school = intent.getSlot("school").getValue()
        if (school == null) {
            school = "Elementary"
        }
        String meal = intent.getSlot("meal").getValue()
        if (meal == null) {
            meal = "Lunch"
        }
        String when = intent.getSlot("when").getValue()
        if (when == null) {
            when = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())
        }

        try {
            //http://ec2-54-88-151-2.compute-1.amazonaws.com/ec2-1.1-SNAPSHOT/v1/rest/lunch

            def menuText = doPost(meal, school, when)
            println("################################################################Menu text: $menuText")

            SimpleCard card = new SimpleCard()
            card.setTitle(school)
            card.setContent(menuText)

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
            speech.setText(menuText)

            return SpeechletResponse.newTellResponse(speech, card)
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doPost(String meal = 'Lunch', String school, String when) {
        String ret = null
        def baseUrl = "http://ec2-54-88-151-2.compute-1.amazonaws.com/ec2-1.1-SNAPSHOT/v1/rest/$meal"
        def http = new HTTPBuilder(baseUrl)

        PostRequestModel request = new PostRequestModel()
        request.when = when
        request.school = school
        String json = new Gson().toJson(request)

        // perform a POST request, expecting TEXT response
        http.request(Method.POST, ContentType.JSON) {
            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            it.body = json

            // response handler for a success response code
            it.response.success = { resp, reader ->
                println "response status: ${resp.statusLine}"
                println 'Headers: -----------'
                resp.headers.each { h ->
                    println " ${h.name} : ${h.value}"
                }

                ret = reader.getText()

                println 'Response data: -----'
                println ret
                println '--------------------'
            }
        }
        return ret
    }

    public SpeechletResponse getHelpResponse() {
        return null;
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return null
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }
}