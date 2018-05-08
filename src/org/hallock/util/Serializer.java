package org.hallock.util;

import org.json.JSONObject;

import java.awt.*;

public class Serializer {
    public static Rectangle parseRectangle(JSONObject obj) {
        return new Rectangle(
                obj.getInt("x"),
                obj.getInt("y"),
                obj.getInt("w"),
                obj.getInt("h")
        );
    }
}
