package org.hallock.dota.control;

import org.hallock.dota.view.ImageSelector;
import org.hallock.dota.view.Ui;
import org.json.JSONObject;

public class UiBuilder {
    public static Ui buildUi(JSONObject uiSettings) {
        ImageSelector imageSelector = ImageSelector.createImageSelector();
        Ui ui = new Ui();
        return ui;
    }
}
