package org.hallock.dota.control.others;

import org.hallock.dota.control.Registry;
import org.hallock.dota.util.Cameras;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ManuallyClassify {

    private static int runnableCount = 0;

    private static class ClassifyImageRunnable implements Runnable {

        private final Runnable next;
        private final String imageFile;
        private final int num;

        ClassifyImageRunnable(String imageFile, Runnable next, int num) {
            this.next = next;
            this.imageFile = imageFile;
            this.num = num;
        }


        public void run() {
            if (false) {
                try {
                    System.out.println("Waiting to continue...");
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Running executable " + num);
            try {
                System.out.println("Opening " + imageFile);
                BufferedImage image = ImageIO.read(new File(imageFile));
                Registry.getInstance().ui.showImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                Registry.getInstance().camera = Cameras.buildCamera(imageFile);
            } catch (AWTException | IOException e) {
                e.printStackTrace();
            }
            Registry.getInstance().ui.identify(next);
            System.out.println("\t\tDone with executable " + num + " of " + runnableCount);
        }
    }

    public static void classify() throws IOException, AWTException {
        Registry.getInstance().ui.debug();
        Runnable currentRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Shutting down");
                System.exit(0);
            }
        };
        String[] images = GetDistances.collectImages("./screenshots");
        shuffleArray(images);
        for (String imageFile : images) {
            if (imageFile.substring("./screenshots/".length()).startsWith("other")) {
                System.out.println("Skipping " + imageFile);
                continue;
            }
            currentRunnable = new ClassifyImageRunnable(imageFile, currentRunnable, ++runnableCount);
        }
        currentRunnable.run();
    }

    private static void shuffleArray(String[] arr) {
        for (int i=0;i<2 * arr.length;i++) {
            int i1 = Registry.getInstance().random.nextInt(arr.length);
            int i2 = Registry.getInstance().random.nextInt(arr.length);
            if (i1 == i2) {
                continue;
            }
            String s1 = arr[i2];
            arr[i2] = arr[i1];
            arr[i1] = s1;
        }
    }
}
