package com.coloredpanda.utils;

import java.util.Objects;

public class HtmlHelper {

    public static String getItalic(String text) throws NullPointerException {
        Objects.requireNonNull(text);
        return "<i>" + text + "</i>";
    }

    public static String getBold(String text) throws NullPointerException {
        Objects.requireNonNull(text);
        return "<b>" + text + "</b>";
    }

    public static String getLinkedText(String text, String url) throws NullPointerException {
        if (text == null || url == null) throw new NullPointerException();
        return "<a href=\"" + url + "\">" + text + "</a>";
    }

}
