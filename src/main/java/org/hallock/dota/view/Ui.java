package org.hallock.dota.view;


import org.hallock.dota.control.Registry;
import org.hallock.dota.control.others.TakeImages;
import org.hallock.dota.model.Identifications;
import org.hallock.dota.model.UnIdentifiedImage;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Ui {
    MainPanel mainPanel;
    JFrame mainFrame;
    Identifications.IdentificationResults results;


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

    public void showUnidentifiedHeroes(LinkedList<UnIdentifiedImage> unIdentifiedImages) {
        PickGuesses guesses = (PickGuesses) getView(Ui.PICK_GUESSES_VIEW).getPanel();
        guesses.setGuesses(unIdentifiedImages);
        show(Ui.PICK_GUESSES_VIEW);
    }

    public void setPicks(Identifications.IdentificationResults results, LinkedList<UnIdentifiedImage> unidentified) {
        if (!unidentified.isEmpty()) {
            showUnidentifiedHeroes(unidentified);
        }
        DebugPane guesses = (DebugPane) getView(Ui.DEBUG_VIEW).getPanel();
        guesses.setResults(results);
        guesses.refresh();
        this.results = results;
    }

    public void identify() {
        try {
            TakeImages.takeSomePictures();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Registry.getInstance().picker.identifyPicks();
    }

    public void post() {
        if (results == null) {
            Registry.getInstance().logger.log("No results to post. Please identify first.");
            return;
        }
        try {
            Registry.getInstance().networkManager.sendPicks(results, null);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        mainFrame.setVisible(true);
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
    static final String NAME_CONFIG_VIEW = "names-preview";
    static final String PICK_CONFIG_VIEW = "pick-preview";
    static final String PICK_GUESSES_VIEW = "pick-guesses";
    static final String DEBUG_VIEW = "debug-view";
}