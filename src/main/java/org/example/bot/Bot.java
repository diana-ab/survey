package org.example.bot;

import org.example.community.CommunityRegistry;
import org.example.community.CommunityService;
import org.example.config.Config;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private CommunityService communityService;
    private CommunityRegistry registry;

    public Bot(CommunityService communityService, CommunityRegistry registry) {
        this.communityService = communityService;
        this.registry = registry;
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
        if (update == null || !update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String name = (update.getMessage().getFrom() == null) ? null
                : (("" + update.getMessage().getFrom().getFirstName() + " "
                + update.getMessage().getFrom().getLastName()).trim());
        String text = update.getMessage().getText();

        JoinOutcome outcome = communityService.handleRegister(chatId, name, text);
        switch (outcome) {
            case ADDED_NEW_MEMBER -> {
                int size = registry.getMembers().size();
                String joinedName = (name == null || name.isBlank()) ? ("User " + chatId) : name;
                String note = "🟢 " + joinedName + " joined. Community size: " + size;

                registry.getMembers().forEach(m -> {
                    if (m.getChatId() == chatId) return;
                    trySend(m.getChatId(), note);
                });

                trySend(chatId, "✅ Welcome, " + joinedName + "! You’re registered.\nCommunity size: " + size);
            }
            case ALREADY_MEMBER -> System.out.println("Member already registered: " + chatId);
            default -> {
            }
        }
    }

    private void trySend(long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException ignored) {
        }
    }
}
