package com.aka.DollarToRupee;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class DollarRupeeSpeechletRequestHandler extends SpeechletRequestStreamHandler {
    public static final Logger log = LoggerFactory.getLogger(DollarRupeeSpeechletRequestHandler.class);
    private static final Set<String> supportedApplicationIds;
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        log.info("Inside static block");
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.a971cb4d-c8f0-4707-a35a-7f07db91e652");
    }

    public DollarRupeeSpeechletRequestHandler() {
        super(new DollarRupeeSpeechlet(), supportedApplicationIds);
        log.info("Inside DollarRupeeSpeechlet");
    }
}
