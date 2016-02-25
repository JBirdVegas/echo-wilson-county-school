package com.jbirdvegas.echo;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public final class SchoolMenuRequestStreamHandler extends SpeechletRequestStreamHandler {
    static final Set<String> supportedApplicationIds = new HashSet<>();
    static {
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.1505862f-cbd5-4e21-a47f-444135a31258");
    }

    public SchoolMenuRequestStreamHandler() {
        super(new SchoolMenuSpeechlet(), supportedApplicationIds);
    }
}