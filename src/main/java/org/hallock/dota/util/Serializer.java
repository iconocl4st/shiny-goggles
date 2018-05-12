package org.hallock.dota.util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serializer {
    public static Rectangle parseRectangle(JSONObject obj) {
        return new Rectangle(
                obj.getInt("x"),
                obj.getInt("y"),
                obj.getInt("w"),
                obj.getInt("h")
        );
    }

    public static JSONObject readFile(Path path) throws IOException {
        try (InputStream reader = Files.newInputStream(path);) {
            return new JSONObject(new JSONTokener(reader));
        }
    }
}
