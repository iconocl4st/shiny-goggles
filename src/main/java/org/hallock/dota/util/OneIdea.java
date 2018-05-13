package org.hallock.dota.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OneIdea {
    public static double getDistance(
            BufferedImage image1,
            BufferedImage image2,
            int parameter
    ) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            System.out.println("Images have different sizes:");
            System.out.println(String.join(",", String.valueOf(image1.getWidth()), String.valueOf(image1.getHeight())));
            System.out.println(String.join(",", String.valueOf(image2.getWidth()), String.valueOf(image2.getHeight())));
            return Double.POSITIVE_INFINITY;
        }


        double totalPixels = image1.getWidth() * image2.getWidth();
        double ssq = 0;
        // Assuming they are the same size
        for (int i = 0; i<image1.getWidth(); i++) {
            for (int j=0;j<image1.getHeight(); j++) {
                double minimum = Double.POSITIVE_INFINITY;
                Color originalColor = new Color(image1.getRGB(i, j));
                for (int si = i - parameter; si < i + parameter; si++) {
                    if (si < 0 || si >= image1.getWidth()) {
                        continue;
                    }
                    for (int sj = j - parameter; sj < j + parameter; sj++) {
                        if (sj < 0 || sj >= image1.getHeight()) {
                            continue;
                        }
                        Color comparisonColor = new Color(image2.getRGB(si, sj));
                        int dr = originalColor.getRed() - comparisonColor.getRed();
                        int dg = originalColor.getGreen() - comparisonColor.getGreen();
                        int db = originalColor.getBlue() - comparisonColor.getBlue();
                        double distance = dr * dr + dg * dg + db * db;
                        if (distance < minimum) {
                            minimum = distance;
                        }
                    }
                }
                ssq += minimum / totalPixels;
            }
        }
        return ssq;
    }
}
