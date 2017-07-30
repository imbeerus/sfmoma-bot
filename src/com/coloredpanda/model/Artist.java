package com.coloredpanda.model;

import com.coloredpanda.services.StringService;
import com.coloredpanda.utils.HtmlHelper;
import com.coloredpanda.utils.LogHelper;
import com.coloredpanda.utils.WebUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

import static com.coloredpanda.utils.StringHelper.getNullableString;

public class Artist extends JsonObject implements MessageObject {

    private static final String TAG = LogHelper.makeLogTag(Artist.class);

    private String mName;
    private String mWebUrl;
    private String mLifeInfo;
    private String mBiography;
    private String mNationality;

    private List<String> mArtworks;

    public Artist(String json) {
        this(json, false);
    }

    public Artist(String json, boolean isResultPage) {
        super(json, isResultPage);
        initArtworks(jsonObject);
        initArtistInfo(jsonObject);
    }

    private void initArtistInfo(JSONObject artist) {
        mWebUrl = artist.getString("web_url");
        mName = artist.getJSONObject("name").getString("display");
        mLifeInfo = artist.getJSONObject("life_info").getString("display");
        mBiography = artist.getJSONObject("biography").getString("summary");
        mNationality = artist.getJSONObject("background").getString("nationality");
    }

    private void initArtworks(JSONObject artist) {
        JSONArray artworksArray = artist.getJSONArray("artworks");
        mArtworks = new ArrayList<>();
        for (int i = 0; i < artworksArray.length(); i++) {

            JSONObject artwork = artworksArray.getJSONObject(i).getJSONObject("artwork");
            String jsonUrl = artwork.getString("url");
            String text = artwork.getString("title_display");

            String webUrl = null;
            if (jsonUrl != null && !jsonUrl.trim().isEmpty()) try {
                webUrl = new JSONObject(WebUtils.getSfmomaResponse(jsonUrl)).getString("web_url");
            } catch (Exception e) {
                BotLogger.error(TAG, e);
            }

            if (webUrl != null && !webUrl.trim().isEmpty()) mArtworks.add(HtmlHelper.getLinkedText(text, webUrl));
        }
    }

    private String getPermanentUrl() {
        return getNullableString(mWebUrl);
    }

    private String getName() {
        return getNullableString(mName);
    }

    private String getNationality() {
        return getNullableString(mNationality);
    }

    private String getLifeInfo() {
        return getNullableString(mLifeInfo);
    }

    private String getBiography() {
        return getNullableString(mBiography);
    }

    public List<String> getArtworks() {
        if (mArtworks.isEmpty()) mArtworks.add("");
        return mArtworks;
    }

    @Override
    public String getText() {
        return String.format(StringService.getString("onArtistCommand"),
                 getName(), getNationality(), getLifeInfo(), getBiography(), getPermanentUrl());
    }


}
