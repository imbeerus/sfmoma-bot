package com.coloredpanda.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getFormatedDate(String string, String pattern) {
        DateFormat originalFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        DateFormat resultFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String result;
        try {
            Date date = originalFormat.parse(string);
            result = resultFormat.format(date);
        } catch (ParseException e) {
            result = string;
        }
        return result;
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
