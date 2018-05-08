package org.hallock.control;

import org.hallock.control.ApplicationContext;
import org.hallock.control.Config;
import org.hallock.model.AutoPicker;
import org.hallock.util.Camera;
import org.hallock.view.ImageSelector;
import org.hallock.view.Ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        Config config = Config.loadConfig(Paths.get("./config/config.json"));
        Ui ui = Ui.buildUi();

        Robot robot = new Robot();
        Camera camera = new Camera(robot);
        AutoPicker picker = new AutoPicker(camera);

        ImageSelector imageSelector = ImageSelector.createImageSelector();
//        imageSelector.show();

        int heroHorizontalGap = 7;
        int heroVerticalGap = 10;
        int heroWidth = 45;
        int heroHeight = 70;
        int heroTypeGap = 34;

        int heroStartX = 172;
        int heroStartY = 188;

        int currentY = heroStartY;
        int currentX = heroStartX;

        int imageCounter = 0;
        for (int type = 0; type < 3; type++) {
            for (int h = 0; h < 21; h++) {
                Rectangle r = new Rectangle(currentX, currentY, heroWidth, heroHeight);
                imageSelector.setRectangle(r);
                ImageIO.write(robot.createScreenCapture(r), "png", new File("hero_" + imageCounter++ + ".png"));

                Thread.sleep(200);
                currentX += heroWidth + heroHorizontalGap;
            }
            currentX = heroStartX;
            currentY += heroHeight + heroVerticalGap;

            for (int h = 0; h < 21; h++) {
                Rectangle r = new Rectangle(currentX, currentY, heroWidth, heroHeight);
                imageSelector.setRectangle(r);
                ImageIO.write(robot.createScreenCapture(r), "png", new File("hero_" + imageCounter++ + ".png"));

                Thread.sleep(200);
                currentX += heroWidth + heroHorizontalGap;
            }
            currentX = heroStartX;
            currentY += heroTypeGap + heroHeight;
        }


//        ApplicationContext context = new ApplicationContext(
//                config,
//                ui,
//                picker
//        );
//
//        mainLoop(context);
    }

    public static void mainLoop(ApplicationContext context) {
        while (!context.stop()) {
//            final long lastRefreshTime = context.getLastRefreshTime();
//
//            if (context.needsPick()) {
//
//            }
//            final long lastpickTime = context.getLastPickTime();
            context.picker.update();
            try {
                Thread.sleep(context.config.getWaitTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}