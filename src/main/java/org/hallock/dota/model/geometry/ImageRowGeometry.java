package org.hallock.dota.model.geometry;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageRowGeometry {
    public int startX = 0;
    public int startY = 0;
    public int width = 120;
    public int height = 72;
    public int horizontalGap = 5;
    public int teamGap = 100;

    public static ImageRowGeometry parseGeometry(JSONObject geometry) throws JSONException {
        ImageRowGeometry returnValue = new ImageRowGeometry();
        returnValue.startX = geometry.getInt("start-x");
        returnValue.startY = geometry.getInt("start-y");
        returnValue.width = geometry.getInt("width");
        returnValue.height = geometry.getInt("height");
        returnValue.horizontalGap = geometry.getInt("horizontal-gap");
        returnValue.teamGap = geometry.getInt("team-gap");
        return returnValue;
    }
}
