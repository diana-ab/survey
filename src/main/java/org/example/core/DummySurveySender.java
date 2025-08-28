package org.example.core;

import org.example.model.Question;
import org.example.model.Survey;

/**
 * EXPLANATION: Simple console stub so this project runs without Telegram.
 * Replace with your BotEngine-backed sender in your main project.
 */
public class DummySurveySender implements SurveySender {

    @Override
    public void sendSurvey(Survey survey) throws Exception {
        System.out.println(">> SENDING SURVEY: " + survey.getTitle() + " (id=" + survey.getId() + ")");
        for (Question q : survey.getQuestions()) {
            System.out.println("   Q" + q.getId() + ": " + q.getText() + " -> " + q.getOptions());
        }
    }

    @Override
    public void closeSurvey(long surveyId) throws Exception {
        System.out.println(">> CLOSING SURVEY id=" + surveyId);
    }
}
