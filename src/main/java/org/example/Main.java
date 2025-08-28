package org.example;

import org.example.bot.Bot;
import org.example.bot.TelegramGateway;
import org.example.community.Community;
import org.example.community.CommunityRegistry;
import org.example.community.CommunityService;
import org.example.core.SurveySender;
import org.example.core.TelegramSurveySender;
import org.example.gui.AppFrame;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;

/**
 * Entry point: boots a simple Telegram bot for community registration,
 * then opens the Swing UI. When you press "Send", it broadcasts polls
 * to all chat IDs that sent '/start' or 'hi' to your bot.
 */
public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set up community (join by /start)
                CommunityRegistry registry = new CommunityRegistry();
                CommunityService service = new CommunityService(registry);
                Bot bot = new Bot(service);
                // Register the bot
                TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
                api.registerBot(bot);

                // Wire the survey sender
                Community community = new Community(registry);
                SurveySender sender = new TelegramSurveySender(new TelegramGateway(bot), community);

                // Launch UI
                new AppFrame(sender).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
