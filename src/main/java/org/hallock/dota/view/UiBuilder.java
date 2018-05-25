package org.hallock.dota.view;

import org.hallock.dota.control.Registry;
import org.hallock.dota.util.Logger;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UiBuilder {
    private static void createMainFrame(final Ui ui, JSONObject uiSettings) {
        ui.mainPanel = new MainPanel();

        ui.mainFrame = new JFrame("Auto picker");
        ui.mainFrame.setBounds(new Rectangle(2000, 50, 500, 500));
        ui.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Util.setContent(ui.mainFrame, ui.mainPanel);
        ui.mainFrame.pack();
        Registry.getInstance().logger.addOutput(new Logger.LogOutput() {
            @Override
            public void log(String string) {
                ui.mainPanel.setLog(string);
            }

            @Override
            public void log(String string, Throwable ex) {
                ui.mainPanel.setLog("Error: " + string);
            }
        });
    }

    private static BufferedImage getPreviewImage() {
        try {
            return ImageIO.read(new File("./screenshots/output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage returnValue = new BufferedImage(
                Registry.getInstance().config.SCREEN_WIDTH,
                Registry.getInstance().config.SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D graphics = returnValue.createGraphics();
        graphics.setColor(Color.white);
        graphics.drawString(
                "Preview image unavailable",
                Registry.getInstance().config.SCREEN_WIDTH/ 2,
                Registry.getInstance().config.SCREEN_HEIGHT / 2
        );
        return returnValue;
    }

    private static void createView(
            Ui ui,
            JSONObject object,
            String id,
            String name,
            Ui.View view
    ) {
        Ui.ViewContainer viewContainer = new Ui.ViewContainer(new JFrame(name), view);
        viewContainer.frame.setBounds(new Rectangle(2000, 50, 500, 500));
        viewContainer.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Util.setContent(viewContainer.frame, viewContainer.getPanel());
        viewContainer.frame.pack();
        ui.views.put(id, viewContainer);
    }

    public static Ui buildUi(JSONObject uiSettings) throws IOException {
        final Ui ui = new Ui();

        createView(ui, uiSettings, Ui.IMAGE_PREVIEW, "Image preview", new ImageViewer());
        createView(ui, uiSettings, Ui.CONFIGURAION_VIEW,"Configuration Options", new ConfigureOptions());
        createView(ui, uiSettings, Ui.ACCOUNT_SETTINGS_VIEW,"Account Settings", new AccountSettings());
        createView(ui, uiSettings, Ui.DEBUG_VIEW, "Debug View", new DebugPane());
        createView(ui, uiSettings, Ui.PICK_GUESSES_VIEW, "Pick Guesses", new PickGuesses());
        createView(ui, uiSettings, Ui.GRID_CONFIG_VIEW,"Grid Settings", new GridConfig());
        createView(ui, uiSettings, Ui.NAME_CONFIG_VIEW, "Name Settings", new RowConfig.PlayerRowConfig());
        createView(ui, uiSettings, Ui.PICK_CONFIG_VIEW, "Picked Settings", new RowConfig.PickRowConfig());
        createView(ui, uiSettings, Ui.GRID_PREVIEW,"Preview", new GridPreviewer(getPreviewImage()));
        createMainFrame(ui, uiSettings);

        ui.views.get(Ui.GRID_PREVIEW).frame.setBounds(0, 0, 1920, 1200);
        ui.views.get(Ui.IMAGE_PREVIEW).frame.setBounds(0, 0, 1920, 1200);

        ComponentListener closePreview = new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent componentEvent) {
                ui.hide(Ui.GRID_PREVIEW);
            }
        };
        ui.views.get(Ui.GRID_CONFIG_VIEW).frame.addComponentListener(closePreview);
        ui.views.get(Ui.NAME_CONFIG_VIEW).frame.addComponentListener(closePreview);
        ui.views.get(Ui.PICK_CONFIG_VIEW).frame.addComponentListener(closePreview);
        ui.views.get(Ui.PICK_GUESSES_VIEW).frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                PickGuesses guesses = (PickGuesses) ui.views.get(Ui.PICK_GUESSES_VIEW).getPanel();
                guesses.resizeComponents();
            }
        });

        ui.imageSelector = ImageSelector.createImageSelector();

        return ui;
    }
}
