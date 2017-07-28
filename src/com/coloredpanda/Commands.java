package com.coloredpanda;


import com.coloredpanda.services.StringService;
import com.coloredpanda.utils.Emoji;

public class Commands {

    public static final String commandInitChar = "/";

    public static final String help = commandInitChar + "help";
    public static final String startCommand = commandInitChar + "start";

    public static String getArtworkCommand() {
        return String.format(StringService.getString("artwork"), Emoji.FRAMED_PICTURE.toString());
    }

    public static String getArtistCommand() {
        return String.format(StringService.getString("artist"), Emoji.MAN.toString());
    }

}
