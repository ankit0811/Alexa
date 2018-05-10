 package com.aka.DollarToRupee;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DollarRupeeSpeechlet implements SpeechletV2 {
    private static final Logger log = LoggerFactory.getLogger(DollarRupeeSpeechlet.class);
    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope) {

        log.info("onSessionStarted requestId={}, sessionId={}", speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope) {
        log.info("onLaunch requestId={}, sessionId={}", speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());
        //return getAskResponse("Dollar To Rupee", "I am inside sessionLaunch");
        return getDollarToRupeeResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope) {
        IntentRequest request = speechletRequestEnvelope.getRequest();
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), speechletRequestEnvelope.getSession().getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent!=null) ? intent.getName() : null;

        log.debug("Test Ank: "+ intentName);
        if("GetDollarToRupee".equals(intentName)){
            return getDollarToRupeeResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)){
            return getHelpResponse();
        } else if ("AMAZON.StopIntent".equals(intentName)){
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else if ("AMAZON.CancelIntent".equals(intentName)){
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            return getAskResponse("DollarRupee", "This is unsupported. Please try something else.");
        }
    }


    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope) {
        log.info("onSessionEnd requiestId={}, sessionsId={}", speechletRequestEnvelope.getRequest().getRequestId(),
                speechletRequestEnvelope.getSession().getSessionId());


    }

    private SpeechletResponse getDollarToRupeeResponse(){
        GetTopExchanges dollarRupee = new GetTopExchanges();
        Map<String, Double> map = dollarRupee.getExchangeRateMap();

        StringBuilder outputText= new StringBuilder();
        outputText.append("Mid Market Rate for today is ").append(dollarRupee.getMmRate()).append("..\n");
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            outputText.append(entry.getKey()).append(" with rate of ").append(entry.getValue()).append("..\n");
        }

        SimpleCard card = getSimpleCard("DollarToRupee", outputText.toString());

        PlainTextOutputSpeech speech = getPlainOutputSpeech(outputText.toString());

        return SpeechletResponse.newTellResponse(speech, card);
        }

    private SpeechletResponse getHelpResponse(){
        String speechText = "You can ask for GetDollarToRupee or you can say exit. What can I help you with?";
        return getAskResponse("DollarToRupee", speechText);
    }

    private SimpleCard getSimpleCard(String title, String content){
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(content);

        return card;
    }

    private PlainTextOutputSpeech getPlainOutputSpeech(String speechText){
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return speech;
    }

    private Reprompt getReprompt(OutputSpeech speech){
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return reprompt;

    }
    private SpeechletResponse getAskResponse(String cardTitle, String speechText){
        SimpleCard card = getSimpleCard(cardTitle, speechText);
        PlainTextOutputSpeech speech = getPlainOutputSpeech(speechText);

        Reprompt reprompt = getReprompt(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
