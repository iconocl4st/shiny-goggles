package org.hallock.dota.model;

import org.hallock.dota.util.Cameras;

import java.awt.*;
import java.awt.image.BufferedImage;

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
    public void identify(IdentificationContext context, Cameras.Camera camera, Identifications pickedState) {
        BufferedImage image = camera.shoot(location);

        int count = 0;
        for (int i=0;i<image.getWidth();i++) {
            for (int j=0;j<image.getHeight();j++) {
                Color color = new Color(image.getRGB(i, j));
                if (color.getRed() != 255) continue;
                if (color.getGreen() != 255) continue;
                if (color.getBlue() != 255) continue;
                count++;
            }
        }

        if (count < 10) {
            return;
        }
        if (count > 100) {
            pickedState.userIdentified(team);
            return;
        }
        throw new IllegalStateException("Unrecognized number of white pixels: " + count);
    }
}
