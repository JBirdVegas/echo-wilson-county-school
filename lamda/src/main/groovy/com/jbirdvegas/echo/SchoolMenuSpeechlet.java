package com.jbirdvegas.echo;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SchoolMenuSpeechlet implements Speechlet {
    private Logger mLogger = LoggerFactory.getLogger(SchoolMenuSpeechlet.class);
    // Wed Feb 24 00:00:00 UTC 2016

    public SchoolMenuSpeechlet() {
        mLogger.info("School Menu Speechlet");
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : "";
        mLogger.info("Discovered intent named: $intentName");
        switch (intentName) {
            case "AMAZON.HelpIntent":
                return getHelpResponse();
            case "SchoolMenuIntent":
                return getSchoolMenuIntent(intent);
        }
        return null;
    }

    private SpeechletResponse getSchoolMenuIntent(Intent intent) {
        String school = intent.getSlot("school").getValue();
        if (school == null) {
            school = "Elementary";
        }
        String meal = intent.getSlot("meal").getValue();
        if (meal == null) {
            meal = "Lunch";
        }
        String when = intent.getSlot("when").getValue();
        if (when == null) {
            when = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        }

        //http://ec2-54-88-151-2.compute-1.amazonaws.com/ec2-1.1-SNAPSHOT/v1/rest/lunch

        String menuText = MenuGetter.doPost(meal, school, when);
        mLogger.info("################################################################Menu text: $menuText");

        SimpleCard card = new SimpleCard();
        card.setTitle(school);
        card.setContent(menuText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(menuText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse getHelpResponse() {
        return null;
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return null;
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }
}