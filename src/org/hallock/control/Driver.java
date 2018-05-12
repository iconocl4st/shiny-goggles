package org.hallock.control;

import org.hallock.control.ApplicationContext;
import org.hallock.control.Config;
import org.hallock.model.AutoPicker;
import org.hallock.model.Heroes;
import org.hallock.util.Camera;
import org.hallock.util.Logger;
import org.hallock.util.Serializer;
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
        ApplicationContext.applicationContext = new ApplicationContext();
        ApplicationContext.applicationContext.logger = new Logger();
        ApplicationContext.applicationContext.config = new Config(
                Serializer.readFile(Paths.get("./config/config.json"))
        );
        ApplicationContext.applicationContext.heroes = Heroes.buildHeroes(
                Serializer.readFile(Paths.get(ApplicationContext.getInstance().config.heroConfigFile))
        );
        ApplicationContext.applicationContext.picker = AutoPickerBuilder.buildAutoPicker();



        Ui ui = UiBuilder.buildUi();


        Robot robot = new Robot();
        Camera camera = new Camera(robot);
        AutoPicker picker = new AutoPicker(camera);







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

//        int imageCounter = 0;
//        for (int type = 0; type < 3; type++) {
//            for (int h = 0; h < 21; h++) {
//                Rectangle r = new Rectangle(currentX, currentY, heroWidth, heroHeight);
//                imageSelector.setRectangle(r);
//                ImageIO.write(robot.createScreenCapture(r), "png", new File("hero_" + imageCounter++ + ".png"));
//
//                Thread.sleep(200);
//                currentX += heroWidth + heroHorizontalGap;
//            }
//            currentX = heroStartX;
//            currentY += heroHeight + heroVerticalGap;
//
//            for (int h = 0; h < 21; h++) {
//                Rectangle r = new Rectangle(currentX, currentY, heroWidth, heroHeight);
//                imageSelector.setRectangle(r);
//                ImageIO.write(robot.createScreenCapture(r), "png", new File("hero_" + imageCounter++ + ".png"));
//
//                Thread.sleep(200);
//                currentX += heroWidth + heroHorizontalGap;
//            }
//            currentX = heroStartX;
//            currentY += heroTypeGap + heroHeight;
//        }


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