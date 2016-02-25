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
import com.jbirdvegas.echo.common.AnswerModel;
import com.jbirdvegas.echo.common.QuestionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                return getSchoolMenuIntent(null);
            case "SchoolMenuIntent":
                return getSchoolMenuIntent(intent);
        }
        return null;
    }

    private SpeechletResponse getSchoolMenuIntent(Intent intent) {
        // setup default gson model
        QuestionModel question = new QuestionModel();
        // if overrides come from alexa then accept them.
        if (intent != null) {
            String school = intent.getSlot("school").getValue();
            if (school != null) {
                question.school = school;
            }
            String meal = intent.getSlot("meal").getValue();
            if (meal != null) {
                question.meal = meal;
            }
            String when = intent.getSlot("when").getValue();
            if (when != null) {
                question.when = when;
            }
        }

        // call helper to get menu response as gson model
        AnswerModel answerModel = MenuGetter.doPost(question);

        // setup response to user in app
        SimpleCard card = new SimpleCard();
        card.setTitle(String.format("%s %s school", question.meal, question.school));
        card.setContent(answerModel.normalizeForVerbalResponse());

        // setup response to user verbally through alexa
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(answerModel.normalizeForVerbalResponse());

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