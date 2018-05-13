
package org.hallock.dota.control;

import org.hallock.dota.control.others.GetDistances;
import org.hallock.dota.control.others.TakeImages;
import org.hallock.dota.control.others.TestPost;
import org.hallock.dota.model.Heroes;
import org.hallock.dota.util.Cameras;
import org.hallock.dota.util.Logger;
import org.hallock.dota.util.NetworkManager;
import org.hallock.dota.util.Serializer;
import org.hallock.dota.view.UiBuilder;

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
            Registry.registry.camera = Cameras.buildCamera();
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

            Registry.getInstance().picker.identifyPicks();


            switch ("ui") {
                case "distances":
                    GetDistances.getAllDistances();
                    System.exit(0);
                    break;
                case "images":
                    TakeImages.takeSomePictures();
                    System.exit(0);
                    break;
                case "post":
                    TestPost.testPost();
                    System.exit(0);
                    break;
                case "ui":
                default:
                    Registry.getInstance().ui.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }




}