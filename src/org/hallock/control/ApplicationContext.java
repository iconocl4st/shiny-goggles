package org.hallock.control;

import org.hallock.model.AutoPicker;
import org.hallock.util.Camera;
import org.hallock.util.Logger;
import org.hallock.view.Ui;

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
