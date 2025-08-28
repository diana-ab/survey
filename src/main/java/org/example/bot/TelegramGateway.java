package org.example.bot;

import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramGateway implements TelegramGatewayInterface {
    private final Bot bot;

    public TelegramGateway(Bot bot) {
        this.bot = bot;
    }

    @Override
    public synchronized Message sendMessage(SendMessage msg) throws TelegramApiException {
        return bot.execute(msg);
    }

    @Override
    public synchronized Message sendPoll(SendPoll poll) throws TelegramApiException {
        return bot.execute(poll);
    }
}
