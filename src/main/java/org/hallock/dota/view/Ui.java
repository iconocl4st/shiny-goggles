package org.hallock.dota.view;


import org.hallock.dota.control.Registry;

import javax.swing.*;
import java.util.HashMap;

public class Ui {
    MainPanel mainPanel;
    JFrame mainFrame;


    ImageSelector imageSelector;

    HashMap<String, ViewContainer> views = new HashMap<>();

    ViewContainer getView(String name) {
        return views.get(name);
    }

    void show(String name) {
        Registry.getInstance().logger.log("Showing " + name);
        ViewContainer view = getView(name);
        view.panel.refresh();
        view.frame.setVisible(true);
    }

    void hide(String name) {
        Registry.getInstance().logger.log("Hiding " + name);
        ViewContainer view = getView(name);
        view.frame.setVisible(false);
    }

    static class ViewContainer {
        JFrame frame;
        View panel;

        ViewContainer(JFrame frame, View panel) {
            this.frame = frame;
            this.panel = panel;
        }

        JPanel getPanel() {
            return (JPanel) this.panel;
        }
    }

    interface View {
        void refresh();
//        void setVisible(boolean b);
    }

    static final String CONFIGURAION_VIEW = "configure";
    static final String ACCOUNT_SETTINGS_VIEW = "account-settings";
    static final String GRID_CONFIG_VIEW = "grid-config";
    static final String GRID_PREVIEW = "grid-preview";
}