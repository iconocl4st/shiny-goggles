package org.hallock.dota.control;

import org.hallock.dota.model.AutoPicker;
import org.hallock.dota.view.Ui;
import org.hallock.dota.util.Camera;
import org.hallock.dota.util.Logger;

// Rename to registry
public class ApplicationContext {
    public Ui ui;
    public Config config;
    public AutoPicker picker;
    public Camera camera;
    public Logger logger;


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
