package org.example.bot;

import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramGatewayInterface {
    Message sendMessage(SendMessage msg) throws TelegramApiException;

    Message sendPoll(SendPoll poll) throws TelegramApiException;
}
