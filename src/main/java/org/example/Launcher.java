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

public final class Launcher {
    private Launcher() {
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                CommunityRegistry registry = new CommunityRegistry();
                CommunityService service = new CommunityService(registry);

                Bot bot = new Bot(service, registry);
                TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
                api.registerBot(bot);

                TelegramGateway gateway = new TelegramGateway(bot);
                Community community = new Community(registry);
                SurveySender sender = new TelegramSurveySender(gateway, community);

                new AppFrame(sender).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
