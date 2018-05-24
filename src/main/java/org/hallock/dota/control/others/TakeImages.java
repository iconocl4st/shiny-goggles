package org.hallock.dota.control.others;

import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Team;
import org.hallock.dota.model.geometry.GridEnumerator;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TakeImages {
    private static Path getPathOutput(String baseString) {
        int counter = 0;
        Path p;
        while (Files.exists(p = Paths.get("./output/").resolve(
                String.format("%s_%04d.png", baseString, counter)
        ))) {
            counter++;
        }
        return p;
    }


    public static void takeSomePictures() throws JSONException, AWTException, IOException {
        if (!Files.exists(Paths.get("./output"))) {
            Files.createDirectory(Paths.get("./output"));
        }
        final Robot robot = new Robot();
        ImageIO.write(
                robot.createScreenCapture(new Rectangle(0, 0, 1920, 1200)),
                "png",
                getPathOutput("whole_thing").toFile()
        );

        if (true) {
            return;
        }

        GridEnumerator.enumerateGrid(Registry.getInstance().gridGeometry, Registry.getInstance().gridConfig, new GridEnumerator.GridItemVisitor() {
            @Override
            public void visit(JSONObject config, Rectangle location) throws JSONException {
                BufferedImage image = robot.createScreenCapture(location);
                Path path = getPathOutput("unknown_hero_");
                try {
                    ImageIO.write(image, "png", path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        GridEnumerator.enumerateGrid(Registry.getInstance().nameGeometry, new GridEnumerator.RowItemVisitor() {
            @Override
            public void visit(Rectangle location, Team team, int idx) {
                BufferedImage image = robot.createScreenCapture(location);
                Path path = getPathOutput("unknown_name_" + idx + "_" + team.name() + "_");
                try {
                    ImageIO.write(image, "png", path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        GridEnumerator.enumerateGrid(Registry.getInstance().pickedGeometry, new GridEnumerator.RowItemVisitor() {
            @Override
            public void visit(Rectangle location, Team team, int idx) {
                BufferedImage image = robot.createScreenCapture(location);
                Path path = getPathOutput("unknown_pick_" + idx + "_" + team.name() + "_");
                try {
                    ImageIO.write(image, "png", path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
