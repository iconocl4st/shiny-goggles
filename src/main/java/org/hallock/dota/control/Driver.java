
package org.hallock.dota.control;

import org.hallock.dota.model.Heroes;
import org.hallock.dota.model.Identifications;
import org.hallock.dota.model.geometry.GridEnumerator;
import org.hallock.dota.model.geometry.HeroGridGeometry;
import org.hallock.dota.util.Camera;
import org.hallock.dota.util.Logger;
import org.hallock.dota.util.NetworkManager;
import org.hallock.dota.util.Serializer;
import org.hallock.dota.view.ImageViewer;
import org.hallock.dota.view.UiBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Timer;

public class Driver {
    public static void main(String[] args) {
        try {
            Registry.registry = new Registry();
            Registry.registry.logger = Logger.buildLogger();
            Registry.registry.config = new Config(
                    "./config/config.json",
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
            Registry.registry.networkManager = NetworkManager.buildNetworkManager();

            Registry.registry.runner = new Runner(new Timer());

            Registry.registry.runner.start();

            Registry.registry.logger.log("Started Application");

//            // 1, 2, 3, 4, 5, 6
//            // anti-mage, axe, bane, blood-seeker, crystal-maiden
//
//            // earth-shaker, juggernaut, mirana
//            // 7, 8, 9
//
//            // 10, 11
//            // morphling, shadow-fiend
//
//            // 6: drow ranger
//
//            Identifications.IdentificationResults results = new Identifications.IdentificationResults();
//            results.banned.add(1);
//            results.banned.add(2);
//            results.banned.add(3);
//            results.banned.add(4);
//            results.banned.add(5);
//            results.banned.add(6);
//            results.radiantPicked.add(1);
//            results.radiantPicked.add(8);
//            results.radiantPicked.add(9);
//            results.direPicked.add(10);
//            results.direPicked.add(11);
//            results.unavailable.add(12);
//            results.playerIsRadiant = true;
//            Registry.registry.networkManager.sendPicks(results, null);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}