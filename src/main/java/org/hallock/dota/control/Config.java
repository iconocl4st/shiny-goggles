package org.hallock.dota.control;

import org.hallock.dota.model.geometry.HeroGridGeometry;
import org.hallock.dota.model.geometry.ImageRowGeometry;
import org.hallock.dota.util.Serializer;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;

public class Config {
    // Should probably be resources, not strings.
    // TODO
    public String path;

    public String heroConfigFile;
    public String layoutFile;
    public String imageDirectory;
    public String userId;
    public String userCred;
    public String dotaPickerUrl;
    public Rectangle dotaApplication;
    public String uiSettings;
    public int comparisonParameter = 2;
    public String appName;

    public Config(String path, JSONObject config) throws JSONException {
        this.path = path;
        this.heroConfigFile = config.getString("heroes-file");
        this.imageDirectory = config.getString("image-directory");
        this.userId = config.getString("username");
        this.userCred = config.getString("credential");
        this.dotaPickerUrl = config.getString("dota-picker-url");
        this.layoutFile = config.getString("layout-file");
        this.appName = config.getString("application-name");
//        this.dotaApplication = Serializer.parseRectangle(config.getJSONObject("application-location"));
        this.uiSettings = config.getString("ui-settings-file");
    }

    public JSONObject getJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("heroes-file", heroConfigFile);
        object.put("image-directory", imageDirectory);
        object.put("username", userId);
        object.put("credential", userCred);
        object.put("dota-picker-url", dotaPickerUrl);
        object.put("layout-file", layoutFile);
        object.put("ui-settings-file", uiSettings);
        object.put("application-name", appName);
        return object;
    }

    public boolean hasCreds() {
        return userCred.length() > 0 && userId.length() > 0;
    }

    public void save() throws JSONException, IOException {
        Serializer.writeFile(getJson(), Paths.get(path));
    }
}

