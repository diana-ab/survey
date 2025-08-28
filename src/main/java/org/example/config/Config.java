package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class Config {
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String DEFAULT_USERNAME = "SurveyBot";
    private static final String ERROR_MISSING_TOKEN = "BOT_TOKEN is missing. Set it in src/main/resources/config.properties";

    private static final Properties PROPS = new Properties();
    private static boolean loaded = false;

    private Config() {
    }

    private static void loadPropsIfNeeded() {
        if (loaded) return;
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        loaded = true;
    }

    private static String get(String key) {
        loadPropsIfNeeded();
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) return env;
        String fileVal = PROPS.getProperty(key);
        return (fileVal == null || fileVal.isBlank()) ? null : fileVal.trim();
    }

    public static String getBotToken() {
        String token = get(KEY_BOT_TOKEN);
        if (token == null) throw new IllegalStateException(ERROR_MISSING_TOKEN);
        return token;
    }

    public static String getBotUsername() {
        String u = get(KEY_BOT_USERNAME);
        return (u == null || u.isBlank()) ? DEFAULT_USERNAME : u.trim();
    }
}
