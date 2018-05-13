package org.hallock.dota.model;

import org.hallock.dota.util.Cameras;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class UserIdentifier implements StateIdentifier {
    private final Team team;
    private final Rectangle location;
    private final int idx;

    public UserIdentifier(Team team, Rectangle location, int idx) {
        this.team = team;
        this.location = location;
        this.idx = idx;
    }

    @Override
    public void identify(Cameras.Camera camera, Identifications pickedState) {
        BufferedImage image = camera.shoot(location);
        HashSet<String> colors = new HashSet<>();
        for (int i=0;i<image.getWidth();i++) {
            for (int j=0;j<image.getHeight();j++) {
                Color color = new Color(image.getRGB(i, j));

                String colorString = "#" + color.getRed() + "," + color.getGreen() + "," + color.getBlue();
                colors.add(colorString);
            }
        }
        System.out.println(idx + "," + team + "," + location + "," + colors);
    }
}
