package org.hallock.control;

import org.hallock.model.AutoPicker;
import org.hallock.model.Heroes;
import org.hallock.util.Camera;
import org.hallock.util.Logger;
import org.hallock.view.Ui;

import java.util.Random;

// Rename to registry
public class ApplicationContext {
    public Ui ui;
    public Config config;
    public AutoPicker picker;
    public Camera camera;
    public Logger logger;
    public Heroes heroes;
    public Random random;


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
