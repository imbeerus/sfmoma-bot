package com.coloredpanda.services;

import com.coloredpanda.model.Artist;
import com.coloredpanda.model.Artwork;
import com.coloredpanda.utils.WebUtils;
import org.json.JSONObject;

import java.util.Random;

public class SfmomaService {

    private static final String API_URL = "https://www.sfmoma.org/api/collection/%s/?format=json&page=%s";

    private static volatile SfmomaService instance;

    private SfmomaService() {
    }

    public static SfmomaService getInstance() {
        SfmomaService currentInstance;
        if (instance == null) {
            synchronized (SfmomaService.class) {
                if (instance == null) {
                    instance = new SfmomaService();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public Artwork fetchArtwork() throws Exception {
        String queryType = QueryType.ARTWORKS.toString();
        String response = getJsonResponse(queryType);
        return new Artwork(response, true);
    }

    public Artist fetchArtist() throws Exception {
        String queryType = QueryType.ARTISTS.toString();
        String response = getJsonResponse(queryType);
        return new Artist(response, true);
    }

    private String getJsonResponse(String type) throws Exception {
        String pageNumber = String.valueOf(getRandomValue(type));
        String url = String.format(API_URL, type, pageNumber);
        return WebUtils.getResponse(url);
    }

    private int getRandomValue(String type) throws Exception {
        String mainPageUrl = String.format(API_URL, type, "1");
        String mainPage = WebUtils.getResponse(mainPageUrl);
        JSONObject jsonObject = new JSONObject(mainPage);
        float count = jsonObject.getInt("count");

        // Results of one page contains 20 items, usually (artworks or artists).
        // To get a random page number we need to divide count value on number of items (count / 20) and round it
        // Then subtract one for bound in nextInt and add one for result to avoid zero value
        int maxValue = Math.round(count / 20);
        return new Random().nextInt(maxValue - 1) + 1;
    }

    private enum QueryType {
        ARTWORKS("artworks"), ARTISTS("artists");

        private final String text;

        QueryType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

}
