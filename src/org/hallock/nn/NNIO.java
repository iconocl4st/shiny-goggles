package org.hallock.nn;

import org.hallock.model.Hero;
import org.hallock.model.HeroState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.registry.Registry;

public class NNIO {

    private static final class ImageClassification {
        double[][][] pixels;
        int[] encoding

    }

    private static ImageClassification addTrainingClassification() {

    }

    private static double[] averageImage(BufferedImage image) {
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

    private static int countPixelsInImage(int color, BufferedImage image) {
        return countPixelsInImage(new int[]{color}, image)[0];
    }

    private static int[] countPixelsInImage(int[] colors, BufferedImage image) {
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

    private static ImageClassification createClassification() {
        // number of inputs
        // 3 + Registry.getInstance().config.colorsOfInterest.length
        int num = Registry.getInstance().heroes.getNumberOfHeroes();
        int[] ret = new int[
            num +
            1 + // Banned
            1 + // Available
            1 + // PickedDire
            1 + // PickedRadiant
            1   // Unavailable
        ]
    }

    private static int getHeroStateArrayIndex(HeroState state) {
        switch (state) {
            case Banned:
                return 0;
            case Available:
                return 1;
            case PickedDire:
                return 2;
            case PickedRadiant:
                return 3;
            case Unavailable:
                return 4;
            case Picked:
            case Unidentified:
            default:
                throw new IllegalArgumentException("Cannot train data to be " + state.name());
        }
    }
    private static HeroState getHeroStateFromIndex(int idx) {
        switch (idx) {
            case 0:
                return HeroState.Banned;
            case 1:
                return HeroState.Available;
            case 2:
                return HeroState.PickedDire;
            case 3:
                return HeroState.PickedRadiant;
            case 4:
                return HeroState.Unavailable:
            default:
                throw new IllegalArgumentException("Cannot train data to be " + idx);
        }
    }
    private static int getMaxIdx(double[] array, int start, int stop) {
        if (start == stop) {
            throw new IllegalArgumentException("start == stop: " + start);
        }
        double minimum = Double.NEGATIVE_INFINITY;
        int minimumIdx = -1;
        for (int i=start; i<stop; i++) {
            if (array[i] > minimum) {
                minimum = array[i];
                minimumIdx = i;
            }
        }
        if (minimumIdx < 0) {
            throw new IllegalStateException("I don't know what just happened.");
        }
        return minimumIdx - start;
    }

    private static int[] getY(Hero hero, HeroState state) {
        int num = Registry.getInstance().heroes.getNumberOfHeroes();
        int[] returnValue = new int[
                num + 5
        ];
        returnValue[num + getHeroStateArrayIndex(state)] = 1;
        return returnValue;
    }
}
