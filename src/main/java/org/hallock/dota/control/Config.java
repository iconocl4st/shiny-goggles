package org.hallock.dota.control;

import org.hallock.dota.util.Serializer;
import org.json.JSONObject;

import java.awt.*;

public class Config {
    // Should probably be resources, not strings.
    // TODO
    public String heroConfigFile;
    public String imageDirectory;
    public String userId;
    public String userCred;
    public String dotaPickerUrl;
    public Rectangle dotaApplication;
    public int comparisonParameter = 2;

    public Config(JSONObject config) {
        this.heroConfigFile = config.getString("hero-config-file");
        this.imageDirectory = config.getString("image-directory");
        this.userId = config.getString("username");
        this.userCred = config.getString("credential");
        this.dotaPickerUrl = config.getString("dota-picker-url");
        this.dotaApplication = Serializer.parseRectangle(config.getJSONObject("application-location"));
    }

    public long getWaitTime() {
        return 20 * 1000;
    }
}

