package org.hallock.dota.control;

import org.hallock.dota.model.AutoPicker;
import org.hallock.dota.model.Heroes;
import org.hallock.dota.model.geometry.HeroGridGeometry;
import org.hallock.dota.model.geometry.ImageRowGeometry;
import org.hallock.dota.util.Camera;
import org.hallock.dota.util.Logger;
import org.hallock.dota.util.NetworkManager;
import org.hallock.dota.view.Ui;
import org.json.JSONObject;

import javax.swing.*;

public class Registry {
    public Ui ui;
    public Config config;
    public Camera camera;
    public Logger logger;
    public Heroes heroes;
    public AutoPicker picker;
    public Runner runner;


    public HeroGridGeometry gridGeometry;
    public ImageRowGeometry pickedGeometry;
    public ImageRowGeometry nameGeometry;
    public JSONObject gridConfig;
    public NetworkManager networkManager;


    public boolean stop() {
        return false;
    }

    public boolean needsPick() {
        return true;
    }

    public void refresh() {
    }

    static Registry registry;
    public static Registry getInstance() {
        return registry;
    }
}
