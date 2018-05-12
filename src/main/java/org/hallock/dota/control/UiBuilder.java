package org.hallock.dota.control;

import org.hallock.dota.view.ImageSelector;
import org.hallock.dota.view.Ui;

public class UiBuilder {




    public static Ui buildUi() {

        ImageSelector imageSelector = ImageSelector.createImageSelector();

        Ui ui = new Ui();

        return ui;
    }
}
