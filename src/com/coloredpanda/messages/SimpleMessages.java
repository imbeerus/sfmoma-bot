package com.coloredpanda.messages;

import com.coloredpanda.services.StringService;
import com.coloredpanda.utils.Emoji;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;

public class SimpleMessages {

    public static SendMessage getTextMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.enableHtml(true);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendMessage getReplyTextMessage(Message message, ReplyKeyboard replyKeyboard, String text) {
        SendMessage sendMessage = getTextMessage(message, text);
        if (replyKeyboard != null) {
            sendMessage.setReplyMarkup(replyKeyboard);
        }
        return sendMessage;
    }

    public static SendPhoto getPhotoMessage(Message message) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        return sendPhoto;
    }

    public static SendPhoto getReplyPhotoMessage(Message message, ReplyKeyboard replyKeyboard) {
        SendPhoto sendPhoto = getPhotoMessage(message);
        if (replyKeyboard != null) {
            sendPhoto.setReplyMarkup(replyKeyboard);
        }
        return sendPhoto;
    }

    public static SendMessage getHideKeyboardMessage(Message message) {
        String hideText = Emoji.WAVING_HAND_SIGN.toString();
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setSelective(true);
        return getReplyTextMessage(message, replyKeyboardRemove, hideText);
    }

    public static SendMessage getChooseOptionMessage(Message message, ReplyKeyboard replyKeyboard) {
        String chooseOptionText = StringService.getString("chooseOption");
        return getReplyTextMessage(message, replyKeyboard, chooseOptionText);
    }

}
