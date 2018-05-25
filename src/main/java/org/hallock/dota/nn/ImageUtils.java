package org.hallock.dota.nn;

import org.hallock.dota.control.Registry;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    static int countPixelsInImage(int color, BufferedImage image) {
        return countPixelsInImage(new int[]{color}, image)[0];
    }

    static int[] countPixelsInImage(int[] colors, BufferedImage image) {
        int[] returnValue = new int[colors.length];
        for (int i=0;i<image.getWidth();i++) {
            for (int j=0;j<image.getHeight();j++) {
                int color = image.getRGB(i, j);
                for (int k=0;k<colors.length;k++) {
                    if (colors[k] == color) {
                        colors[k]++;
                    }
                }
            }
        }
        return returnValue;
    }

    static float[][][] getFloats(BufferedImage image) {
        float[][][] returnValue = new float[image.getWidth()][image.getHeight()][3];

        for (int i=0;i<image.getWidth();i++) {
            for (int j=0;j<image.getHeight();j++) {
                Color color = new Color(image.getRGB(i, j));
                returnValue[i][j][0] = (float)(color.getRed() / 255.0);
                returnValue[i][j][1] = (float)(color.getGreen() / 255.0);
                returnValue[i][j][2] = (float)(color.getBlue() / 255.0);
            }
        }
        return returnValue;
    }



    static double[] averageImage(BufferedImage image) {
        double[] sums = new double[3];
        for (int i=0;i<image.getWidth();i++) {
            for (int j=0;j<image.getHeight();j++) {
                Color color = new Color(image.getRGB(i, j));
                sums[0] += color.getRed();
                sums[1] += color.getGreen();
                sums[2] += color.getBlue();
            }
        }
        for (int i=0;i<sums.length;i++) {
            sums[i] /= (image.getWidth() * image.getHeight());
        }
        return sums;
    }

    public static BufferedImage scale(BufferedImage image) {
        int desiredWidth = Registry.getInstance().config.IMAGE_WIDTH;
        int desiredHeight = Registry.getInstance().config.IMAGE_HEIGHT;
        if (image.getWidth() == desiredWidth && image.getHeight() == desiredHeight) {
            return image;
        }

        BufferedImage outputImage = new BufferedImage(desiredWidth, desiredHeight, image.getType());
        Graphics2D graphics = outputImage.createGraphics();
        graphics.drawImage(image, 0, 0, desiredWidth, desiredHeight, null);
        return outputImage;
    }
}
