package org.example.bot;

import org.example.community.CommunityService;
import org.example.config.Config;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    private CommunityService communityService;

    public Bot(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Override
    public String getBotToken() {
        return Config.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return Config.getBotUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getFrom() == null ? null :
                    (update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName()).trim();
            String text = update.getMessage().getText();
            JoinOutcome outcome = communityService.handleRegister(chatId, name, text);
            switch (outcome) {
                case ADDED_NEW_MEMBER -> System.out.println("New member joined: " + chatId + " (" + name + ")");
                case ALREADY_MEMBER -> System.out.println("Member already registered: " + chatId);
                default -> {
                }
            }
        }
    }
}
