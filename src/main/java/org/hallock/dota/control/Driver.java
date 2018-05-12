
package org.hallock.dota.control;

import org.hallock.dota.model.Heroes;
import org.hallock.dota.util.Camera;
import org.hallock.dota.util.Logger;
import org.hallock.dota.util.Serializer;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Timer;

public class Driver {
    public static void main(String[] args) throws AWTException, IOException {
        ApplicationContext.applicationContext = new ApplicationContext();
        ApplicationContext.applicationContext.logger = new Logger();
        ApplicationContext.applicationContext.config = new Config(
                Serializer.readFile(Paths.get("./config/config.json"))
        );
        ApplicationContext.applicationContext.camera = Camera.buildCamera();
        ApplicationContext.applicationContext.heroes = Heroes.buildHeroes(
                Serializer.readFile(Paths.get(ApplicationContext.getInstance().config.heroConfigFile))
        );
        ApplicationContext.applicationContext.picker = AutoPickerBuilder.buildAutoPicker(
                Serializer.readFile(Paths.get(ApplicationContext.applicationContext.config.layoutFile))
        );
        ApplicationContext.applicationContext.ui = UiBuilder.buildUi(
                Serializer.readFile(Paths.get(ApplicationContext.applicationContext.config.uiSettings))
        );

        ApplicationContext.applicationContext.runner = new Runner(new Timer());

        ApplicationContext.applicationContext.runner.start();
    }
}