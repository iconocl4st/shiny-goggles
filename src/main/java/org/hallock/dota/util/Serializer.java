package org.hallock.dota.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public class Serializer {
    public static Rectangle parseRectangle(JSONObject obj) throws JSONException {
        return new Rectangle(
                obj.getInt("x"),
                obj.getInt("y"),
                obj.getInt("w"),
                obj.getInt("h")
        );
    }

    public static JSONObject readFile(Path path) throws IOException, JSONException {
        try (Reader reader = new FileReader(path.toFile())) {
            return new JSONObject(new JSONTokener(reader));
        }
    }

    public static void writeFile(JSONObject object, Path path) throws IOException, JSONException {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(object.toString(2));
        }
    }
}
