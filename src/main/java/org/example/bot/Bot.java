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
    private static final String LOG_PREFIX = "[Bot] ";
    private static final String EMOJI_JOINED = "🟢 ";
    private static final String EMOJI_WELCOME = "✅ ";
    private static final String USER_FALLBACK_PREFIX = "User ";

    private static final String NOTE_JOINED_FMT = EMOJI_JOINED + "%s joined. Community size: %d";
    private static final String WELCOME_FMT = EMOJI_WELCOME + "Welcome, %s! You’re registered.\nCommunity size: %d";
    private static final String LOG_ALREADY_MEMBER_FMT = LOG_PREFIX + "Member already registered: %d";

    private final CommunityService communityService;
    private final CommunityRegistry registry;

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
        if (!isTextMessage(update)) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String name = extractFullName(update);
        String text = update.getMessage().getText();

        JoinOutcome outcome = communityService.handleRegister(chatId, name, text);
        switch (outcome) {
            case ADDED_NEW_MEMBER -> {
                int size = registry.getMembers().size();
                String joinedName = displayName(chatId, name);
                String note = String.format(NOTE_JOINED_FMT, joinedName, size);

                registry.getMembers().forEach(m -> {
                    if (m.getChatId() == chatId) return;
                    trySend(m.getChatId(), note);
                });

                String welcome = String.format(WELCOME_FMT, joinedName, size);
                trySend(chatId, welcome);
            }
            case ALREADY_MEMBER -> System.out.println(String.format(LOG_ALREADY_MEMBER_FMT, chatId));
            default -> {
            }
        }
    }


    private static boolean isTextMessage(Update u) {
        return u != null && u.hasMessage() && u.getMessage().hasText();
    }

    private static String extractFullName(Update update) {
        if (update.getMessage().getFrom() == null) return null;
        String first = update.getMessage().getFrom().getFirstName();
        String last = update.getMessage().getFrom().getLastName();
        String fn = (first == null ? "" : first).trim();
        String ln = (last == null ? "" : last).trim();
        String full = (fn + " " + ln).trim();
        return full.isEmpty() ? null : full;
    }

    private static String displayName(long chatId, String name) {
        return (name == null || name.isBlank()) ? (USER_FALLBACK_PREFIX + chatId) : name;
    }

    private void trySend(long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException ignored) {
        }
    }
}
