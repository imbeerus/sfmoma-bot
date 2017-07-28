package com.coloredpanda.model;

import com.coloredpanda.services.StringService;
import com.coloredpanda.utils.HtmlHelper;
import com.coloredpanda.utils.LogHelper;
import com.coloredpanda.utils.Utils;
import com.coloredpanda.utils.WebUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

import static com.coloredpanda.utils.StringHelper.getNullableString;

public class Artwork extends JsonObject implements MessageObject {

    private static final String TAG = LogHelper.makeLogTag(Artwork.class);

    private String mDate;
    private String mType;
    private String mTitle;
    private String mMedium;
    private String mWebUrl;
    private String mCredit;
    private String mArtistName;
    private String mDimensions;

    private List<Image> mImages;

    public Artwork(String json) {
        this(json, false);
    }

    public Artwork(String json, boolean isResultPage) {
        super(json, isResultPage);
        initImages(mainObject);
        initArtists(mainObject);
        initArtworkInfo(mainObject);
    }

    private void initArtworkInfo(JSONObject artwork) {
        mWebUrl = artwork.getString("web_url");
        mType = artwork.getString("type");
        mTitle = artwork.getJSONObject("title").getString("display");
        mMedium = artwork.getJSONObject("medium").getString("display");
        mDimensions = artwork.getJSONObject("dimensions").getString("display");
        mDate = Utils.getFormatedDate(artwork.getString("created_date"), "yyyy-MM-dd");
        mCredit = artwork.getJSONObject("credit").getString("display").replace("\n", "; ");

        try {
            WebUtils.getResponse(mWebUrl);
        } catch (Exception e) {
            mWebUrl = null;
        }
    }

    private void initImages(JSONObject artwork) {
        JSONArray imagesArray = artwork.getJSONArray("images");
        mImages = new ArrayList<>();
        for (int i = 0; i < imagesArray.length(); i++) {

            JSONObject image = imagesArray.getJSONObject(i);
            String imageUrl = image.getString("public_image");
            String creditLine = image.getString("credit_line");
            mImages.add(new Image(imageUrl, creditLine));
        }
    }

    private void initArtists(JSONObject artwork) {
        JSONArray artistsArray = artwork.getJSONArray("artists");

        if (artistsArray.length() > 0) {
            JSONObject artist = artistsArray.getJSONObject(0).getJSONObject("artist");
            String jsonUrl = artist.getString("url");
            String text = artist.getString("name_display");

            String webUrl = null;
            if (jsonUrl != null && !jsonUrl.trim().isEmpty()) try {
                webUrl = new JSONObject(WebUtils.getSfmomaResponse(jsonUrl)).getString("web_url");
            } catch (Exception e) {
                BotLogger.error(TAG, e);
            }

            mArtistName = webUrl != null && !webUrl.trim().isEmpty() ? HtmlHelper.getLinkedText(text, webUrl) : text;
        } else mArtistName = "";
    }

    private String getCredit() {
        return getNullableString(mCredit);
    }

    private String getDateCreated() {
        return getNullableString(mDate);
    }

    private String getClassification() {
        return getNullableString(mType);
    }

    private String getTitle() {
        return getNullableString(mTitle);
    }

    private String getMedium() {
        return getNullableString(mMedium);
    }

    private String getPermanentUrl() {
        return getNullableString(mWebUrl);
    }

    private String getDimensions() {
        return getNullableString(mDimensions);
    }

    private String getArtistName() {
        return mArtistName;
    }

    public List<Image> getImages() {
        if (mImages.isEmpty()) mImages.add(new Image("", ""));
        return mImages;
    }

    @Override
    public String getText() {
        return String.format(StringService.getString("onArtworkCommand"), getTitle(), getArtistName(), getDateCreated(), getClassification(), getMedium(), getDimensions(), getCredit(), getPermanentUrl());
    }

    public static class Image {

        private String mUrl;
        private String mCreditLine;

        Image(String url, String creditLine) {
            mUrl = url;
            mCreditLine = creditLine;
        }

        public String getUrl() {
            return mUrl;
        }

        public String getCreditLine() {
            return getNullableString(mCreditLine);
        }

    }

}