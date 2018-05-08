package org.hallock.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Camera {
    private final Robot robot;

    public Camera(Robot robot) {
        this.robot = robot;
    }

    BufferedImage click(Rectangle rectangle) {
        return robot.createScreenCapture(rectangle);
    }
}
