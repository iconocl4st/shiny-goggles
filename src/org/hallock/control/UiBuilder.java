package org.hallock.control;

import org.hallock.view.ImageSelector;
import org.hallock.view.Ui;

public class UiBuilder {




    public static Ui buildUi() {

        ImageSelector imageSelector = ImageSelector.createImageSelector();

        Ui ui = new Ui();

        return ui;
    }
}
