package org.hallock.dota.control.others;

import org.apache.commons.io.FileUtils;
import org.hallock.dota.util.OneIdea;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

public class GetDistances {
    public static String[] collectImages(String path) {
        LinkedList<String> files = new LinkedList<>();
        for (Iterator<File> it = FileUtils.iterateFiles(
                new File(path), new String[]{"png"}, true); it.hasNext(); ) {
            File file = it.next();
            files.add(file.toString());
        }
        return files.toArray(new String[0]);
    }

    public static void getAllDistances() throws IOException {
        String[] files = collectImages("./config/images");
        try (PrintWriter writer = new PrintWriter(new File("comparisons.txt"))) {
            for (int i = 0; i < files.length; i++) {
                double min = Double.POSITIVE_INFINITY;
                int other = i;
                for (int j = i + 1; j < files.length; j++) {
                    BufferedImage image1 = ImageIO.read(new File(files[i]));
                    BufferedImage image2 = ImageIO.read(new File(files[j]));
                    double distance = OneIdea.getDistance(image1, image2, 2);
                    writer.println("================================================");
                    writer.println(files[i]);
                    writer.println(files[j]);
                    writer.println(distance);
                    writer.println("================================================");

                    if (distance < min) {
                        min = distance;
                        other = j;
                    }
                }
                writer.println("Minimum was " + min + " from " + files[other]);
            }
        }
    }
}
