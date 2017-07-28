package com.coloredpanda.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

abstract class JsonObject {

    JSONObject mainObject;

    JsonObject(String json, boolean isResultPage) {
        if (json != null && !json.trim().isEmpty()) {
            if (isResultPage) {
                JSONArray objectsArray = new JSONObject(json).getJSONArray("results");
                mainObject = objectsArray.getJSONObject(new Random().nextInt(objectsArray.length())); // Get a random object
            } else {
                mainObject = new JSONObject(json);
            }
        }
    }

}
