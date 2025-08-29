package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String DEFAULT_USERNAME = "SurveyBot";
    private static final String CONFIG_RESOURCE = "config.properties";
    private static final String ERROR_MISSING_TOKEN = "BOT_TOKEN is missing. Set it in src/main/resources/config.properties";
    private static final String ERROR_LOAD_FAIL = "Failed to load ";

    private static final Properties PROPS = new Properties();
    private static boolean loaded = false;


    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static void loadPropsIfNeeded() {
        if (loaded) {
            return;
        }

        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException(ERROR_LOAD_FAIL + CONFIG_RESOURCE, e);
        }

        loaded = true;
    }

    private static String get(String key) {
        loadPropsIfNeeded();

        String env = System.getenv(key);
        if (!isBlank(env)) {
            return env.trim();
        }

        String fileVal = PROPS.getProperty(key);
        if (isBlank(fileVal)) {
            return null;
        }

        return fileVal.trim();
    }

    public static String getBotToken() {
        String token = get(KEY_BOT_TOKEN);
        if (token == null) {
            throw new IllegalStateException(ERROR_MISSING_TOKEN);
        }
        return token;
    }

    public static String getBotUsername() {
        String username = get(KEY_BOT_USERNAME);
        if (isBlank(username)) {
            return DEFAULT_USERNAME;
        }
        return username.trim();
    }
}
