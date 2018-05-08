package org.hallock.control;

import org.hallock.model.AutoPicker;
import org.hallock.view.Ui;

public class ApplicationContext {
    Ui ui;
    Config config;
    AutoPicker picker;

    ApplicationContext(Config config, Ui ui, AutoPicker autoPicker) {
        this.ui = ui;
        this.config = config;
        this.picker = autoPicker;
    }


    public boolean stop() {
        return false;
    }

    public boolean needsPick() {
        return true;
    }

    public void refresh() {
    }
}
