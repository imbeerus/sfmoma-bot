package com.coloredpanda.messages;

import com.coloredpanda.services.StringService;
import com.coloredpanda.utils.Emoji;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class BotCommands {

    public static SendMessage getHelpMessage(Message message, ReplyKeyboardMarkup mainKeyboard) {
        String helpText = StringService.getString("helpMessage");
        helpText = String.format(helpText, Emoji.FRAMED_PICTURE.toString(), Emoji.MAN.toString());
        return SimpleMessages.getReplyTextMessage(message, mainKeyboard, helpText);
    }

    public static SendMessage getStartMessage(Message message, ReplyKeyboardMarkup mainKeyboard) {
        String startText = StringService.getString("startMessage");
        startText = String.format(startText, message.getChat().getUserName(), Emoji.WAVING_HAND_SIGN.toString());
        return SimpleMessages.getReplyTextMessage(message, mainKeyboard, startText);
    }

}
