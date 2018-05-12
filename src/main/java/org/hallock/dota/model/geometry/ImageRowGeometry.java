package org.hallock.dota.model.geometry;

import org.json.JSONObject;

public class ImageRowGeometry {
    int startX;
    int startY;
    int width;
    int horizontalGap;



    public static ImageRowGeometry parseGeometry(JSONObject geometry) {
        return new ImageRowGeometry(

        );
    }
}
