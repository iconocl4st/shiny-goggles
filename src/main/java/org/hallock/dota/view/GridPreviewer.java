package org.hallock.dota.view;

import org.hallock.dota.control.Registry;
import org.hallock.dota.model.geometry.GridEnumerator;
import org.hallock.dota.model.geometry.HeroGridGeometry;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class GridPreviewer extends ImageViewer implements Ui.View {
    BufferedImage original;

    GridPreviewer(BufferedImage template) {
        this.original = template;
    }

    void drawGrid(final HeroGridGeometry geometry) throws JSONException {
        BufferedImage image = deepCopy(original);
        final Graphics graphics = image.getGraphics();
        graphics.setColor(Color.red);
        GridEnumerator.enumerateGrid(
                geometry,
                Registry.getInstance().gridConfig,
                new GridEnumerator.GridItemVisitor() {
                    @Override
                    public void visit(JSONObject config, Rectangle location) throws JSONException {
                        graphics.drawString(config.getString("hero"), location.x, location.y + location.height / 2);
                        graphics.drawRect(location.x, location.y, location.width, location.height);

                    }
                }
        );
        setImage(image);
        repaint();
    }

    void clearPreview() {
        setImage(original);
    }


    @Override
    public void refresh() {
        clearPreview();
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
