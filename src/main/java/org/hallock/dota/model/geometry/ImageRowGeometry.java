package org.hallock.dota.model.geometry;

import org.json.JSONObject;

public class ImageRowGeometry {
    public int startX;
    public int startY;
    public int width;
    public int height;
    public int horizontalGap;
    public int teamGap;



    public static ImageRowGeometry parseGeometry(JSONObject geometry) {
        return new ImageRowGeometry(

        );
    }
}
