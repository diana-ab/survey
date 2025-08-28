package org.example.core;

import org.example.community.Community;
import org.example.model.OptionForQuestion;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.bot.TelegramGateway;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class TelegramSurveySender implements SurveySender {
    private TelegramGateway gateway;
    private Community community;

    public TelegramSurveySender(TelegramGateway gateway, Community community) {
        this.gateway = gateway;
        this.community = community;
    }

    @Override
    public void sendSurvey(Survey survey) throws Exception {
        for (long chatId : community.getAllChatIds()) {
            gateway.sendMessage(new SendMessage(String.valueOf(chatId), "📊 New survey: " + survey.getTitle()));
            for (Question q : survey.getQuestions()) {
                List<String> opts = new ArrayList<>();
                for (OptionForQuestion o : q.getOptions()) opts.add(o.getText());
                SendPoll poll = new SendPoll(String.valueOf(chatId), "(" + (q.getId() + 1) + "/" + survey.getQuestions().size() + ") " + q.getText(), opts);
                poll.setIsAnonymous(true);
                Message msg = gateway.sendPoll(poll);
            }
        }
    }

    @Override
    public void closeSurvey(long surveyId) throws Exception {
        for (long chatId : community.getAllChatIds()) {
            try {
                gateway.sendMessage(new SendMessage(String.valueOf(chatId), "✅ Survey " + surveyId + " closed. Thanks!"));
            } catch (TelegramApiException e) {
            }
        }
    }
}
