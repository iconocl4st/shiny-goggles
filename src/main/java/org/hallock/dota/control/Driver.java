
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
        Registry.registry = new Registry();
        Registry.registry.logger = new Logger();
        Registry.registry.config = new Config(
                Serializer.readFile(Paths.get("./config/config.json"))
        );
        Registry.registry.camera = Camera.buildCamera();
        Registry.registry.heroes = Heroes.buildHeroes(
                Serializer.readFile(Paths.get(Registry.getInstance().config.heroConfigFile))
        );
        Registry.registry.picker = AutoPickerBuilder.buildAutoPicker(
                Serializer.readFile(Paths.get(Registry.registry.config.layoutFile))
        );
        Registry.registry.ui = UiBuilder.buildUi(
                Serializer.readFile(Paths.get(Registry.registry.config.uiSettings))
        );

        Registry.registry.runner = new Runner(new Timer());

        Registry.registry.runner.start();
    }
}