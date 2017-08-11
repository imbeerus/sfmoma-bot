package com.coloredpanda;

import com.coloredpanda.updateshandlers.SfmomaHandlers;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class Main {
    private static final String TAG = "MAIN";

    public static void main(String[] args) {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(TAG, e);
        }

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = createLongPollingTelegramBotsApi();
            try {
                // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
                telegramBotsApi.registerBot(new SfmomaHandlers());
            } catch (TelegramApiException e) {
                BotLogger.error(TAG, e);
            }
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }

    private static TelegramBotsApi createLongPollingTelegramBotsApi() {
        return new TelegramBotsApi();
    }

}
