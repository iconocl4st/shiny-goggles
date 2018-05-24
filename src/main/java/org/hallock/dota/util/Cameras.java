package org.hallock.dota.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Cameras {
    public interface Camera {
        BufferedImage shoot(Rectangle location);
    }

    private static final class ImageCamera implements Camera {
        BufferedImage image;

        private ImageCamera(BufferedImage image) {
            this.image = image;
        }

        public BufferedImage shoot(Rectangle location) {
            return image.getSubimage(
                    location.x,
                    location.y,
                    location.width,
                    location.height
            );
        }

    }

    private static final class RobotCamera implements Camera {
        private final Robot robot;

        private RobotCamera(Robot robot) {
            this.robot = robot;
        }

        public BufferedImage shoot(Rectangle location) {
            return robot.createScreenCapture(location);
        }
    }

    public static Camera buildCamera(String path) throws AWTException, IOException {
        if (path != null) {
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageCamera(image);
        } else {
            Robot robot = new Robot();
            return new RobotCamera(robot);
        }
    }
}
