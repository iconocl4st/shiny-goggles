
package org.hallock.dota.control;

import org.hallock.dota.control.others.*;
import org.hallock.dota.model.Heroes;
import org.hallock.dota.util.Cameras;
import org.hallock.dota.util.Logger;
import org.hallock.dota.util.NetworkManager;
import org.hallock.dota.util.Serializer;
import org.hallock.dota.view.UiBuilder;

import java.nio.file.Paths;
import java.util.Random;
import java.util.Timer;

public class Driver {
    public static void main(String[] args) {
        try {
            Registry.registry = new Registry();
            Registry.registry.random = new Random(1776);
            Registry.registry.logger = Logger.buildLogger();
            Registry.registry.config = new Config(
                    "./creds.txt",
                    Serializer.readFile(Paths.get("./creds.txt"))
            );
            Registry.registry.threadManager = ThreadManager.buildThreadPool();
            Registry.registry.camera = Cameras.buildCamera(null);
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

            switch ("nn") {
                case "printLayers":
                    TestSimpleNN.printLayerNames();
                    System.exit(0);
                    break;
                case "createTraining":
                    TestSimpleNN.updateTrainingSet();
                    System.exit(0);
                    break;
                case "getColor":
                    GetImageColor.getImageColor();
                    System.exit(0);
                    break;
                case "distances":
                    GetDistances.getAllDistances();
                    System.exit(0);
                    break;
                case "images":
                    TakeImages.takeSomePictures();
                    System.exit(0);
                    break;
                case "classify":
                    // None should have quite a few images that it doesn't.
                    // The tolerance is too large.
                    ManuallyClassify.classify();
                    break;
                case "nn":
                    TestSimpleNN.runSimpleNN();
                    System.exit(0);
                    break;
                case "post":
                    TestPost.testPost();
                    System.exit(0);
                    break;
                case "progress":
                    PrintProgress.printProgress();
                    System.exit(0);
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