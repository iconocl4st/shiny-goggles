package org.hallock.dota.control;

import org.hallock.dota.model.AutoPicker;
import org.hallock.dota.model.Heroes;
import org.hallock.dota.util.Camera;
import org.hallock.dota.util.Logger;
import org.hallock.dota.view.Ui;

// Rename to registry
public class ApplicationContext {
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

    static ApplicationContext applicationContext;
    public static ApplicationContext getInstance() {
        return applicationContext;
    }
}
