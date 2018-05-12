package org.hallock.dota.util;

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

    public BufferedImage shoot(Rectangle location) {
        return robot.createScreenCapture(location);
    }


    public static Camera buildCamera() throws AWTException {
        Robot robot = new Robot();
        return new Camera(robot);
    }
}
