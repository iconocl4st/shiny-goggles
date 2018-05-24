package org.hallock.dota.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JPanel implements Ui.View {
    private BufferedImage image;

    protected void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        if (image == null) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void refresh() {
        repaint();
    }
}
