package com.coloredpanda.utils;

import com.coloredpanda.BotConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class WebUtils {

    public static String getResponse(String baseUrl) throws Exception {
        return getResponse(baseUrl, BotConfig.SFMOMA_TOKEN);
    }

    public static String getResponse(String baseUrl, String token) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder().url(baseUrl);
        if (token != null) builder.header("Authorization", "Token " + token);

        Response response = okHttpClient.newCall(builder.build()).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

}
