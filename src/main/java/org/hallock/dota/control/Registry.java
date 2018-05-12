package org.hallock.dota.control;

import org.hallock.dota.model.AutoPicker;
import org.hallock.dota.model.Heroes;
import org.hallock.dota.util.Camera;
import org.hallock.dota.util.Logger;
import org.hallock.dota.view.Ui;

public class Registry {
    public Ui ui;
    public Config config;
    public Camera camera;
    public Logger logger;
    public Heroes heroes;
    public AutoPicker picker;
    public Runner runner;


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
