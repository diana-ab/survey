package org.example.core;

import org.example.model.Survey;

/**
 * EXPLANATION: Abstraction for sending/closing a survey (Telegram integration point).
 * The Swing UI calls this; your Bot/Engine should implement it in your project.
 */
public interface SurveySender {
    void sendSurvey(Survey survey) throws Exception;
    void closeSurvey(long surveyId) throws Exception;
}
