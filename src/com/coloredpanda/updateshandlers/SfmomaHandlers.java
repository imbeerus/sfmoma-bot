package com.coloredpanda.updateshandlers;

import com.coloredpanda.BotConfig;
import com.coloredpanda.Commands;
import com.coloredpanda.messages.BotCommands;
import com.coloredpanda.messages.SimpleMessages;
import com.coloredpanda.model.Artist;
import com.coloredpanda.model.Artwork;
import com.coloredpanda.services.SfmomaService;
import com.coloredpanda.services.StringService;
import com.coloredpanda.utils.LogHelper;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

import static com.coloredpanda.Commands.getArtistCommand;
import static com.coloredpanda.Commands.getArtworkCommand;

public class SfmomaHandlers extends TelegramLongPollingBot {

    private static final String TAG = LogHelper.makeLogTag(SfmomaHandlers.class);

    @Override
    public String getBotToken() {
        return BotConfig.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.hasText()) {
                    handleIncomingMessage(message);
                }
            }
        } catch (Exception e) {
            BotLogger.error(TAG, e);
            try {
                String text = StringService.getString("errorMessage") + " ¯\\_(ツ)_/¯";
                sendMessage(SimpleMessages.getTextMessage(update.getMessage(), text));
            } catch (TelegramApiException e1) {
                BotLogger.error(TAG, e1);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BotConfig.NAME;
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        if (!message.isUserMessage() && message.hasText()) {
            if (isCommandForOther(message.getText())) {
                return;
            } else {
                sendMessage(SimpleMessages.getHideKeyboardMessage(message));
                return;
            }
        }

        if (message.getText().startsWith(Commands.commandInitChar)) {
            handleCommandMessage(message);
        } else {
            handleKeyboradMessage(message);
        }
    }

    private static boolean isCommandForOther(String text) {
        boolean isSimpleCommand = "/start".equals(text) || "/help".equals(text) || "/stop".equals(text);
        boolean isCommandForMe = ("/start" + BotConfig.NAME).equals(text) || ("/help" + BotConfig.NAME).equals(text) || ("/stop" + BotConfig.NAME).equals(text);
        return text.startsWith("/") && !isSimpleCommand && !isCommandForMe;
    }

    private void handleCommandMessage(Message message) throws TelegramApiException {
        String text = message.getText();
        SendMessage sendMessageRequest;

        if (text.startsWith(Commands.startCommand))
            sendMessageRequest = BotCommands.getStartMessage(message, getMainMenuKeyboard());
        else sendMessageRequest = BotCommands.getHelpMessage(message, getMainMenuKeyboard());

        sendMessage(sendMessageRequest);
    }

    private void handleKeyboradMessage(Message message) throws TelegramApiException {
        try {
            String text = message.getText();
            if (text.equals(getArtworkCommand())) {
                sendArtwork(message);
            } else if (text.equals(getArtistCommand())) {
                sendArtist(message);
            } else {
                sendMessage(SimpleMessages.getChooseOptionMessage(message, getMainMenuKeyboard()));
            }
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }

    private void sendArtwork(Message message) throws Exception {
        String requestMessage = StringService.getString("requestMessage") + "...";
        sendMessage(SimpleMessages.getTextMessage(message, requestMessage));

        Artwork artwork = SfmomaService.getInstance().fetchArtwork();
        sendMessage(SimpleMessages.getTextMessage(message, artwork.getText()));

        for (Artwork.Image image : artwork.getImages()) {
            if (image.getUrl() != null && !image.getUrl().isEmpty()) {
                SendPhoto sendPhoto = SimpleMessages.getReplyPhotoMessage(message, getMainMenuKeyboard());
                if (!image.getCreditLine().isEmpty()) sendPhoto.setCaption("Credit line: " + image.getCreditLine());
                sendPhoto.setPhoto(image.getUrl());
                sendPhoto(sendPhoto);
            }
        }
    }

    private void sendArtist(Message message) throws Exception {
        String requestMessage = StringService.getString("requestMessage") + "...";
        sendMessage(SimpleMessages.getTextMessage(message, requestMessage));

        Artist artist = SfmomaService.getInstance().fetchArtist();
        sendMessage(SimpleMessages.getTextMessage(message, artist.getText()));

        if (!artist.getArtworks().get(0).isEmpty()) {
            for (String artwork : artist.getArtworks()) {
                sendMessage(SimpleMessages.getReplyTextMessage(message, getMainMenuKeyboard(), artwork));
            }
        }

    }

    private static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getArtworkCommand());
        keyboardFirstRow.add(getArtistCommand());
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
