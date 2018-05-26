package org.hallock.dota.nn;

import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Hero;
import org.hallock.dota.model.HeroState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Features {
    static class Input {
        public final float[][][] pixels;
        public final float[] additionalInformation;


        public Input(float[][][] floats, float[] additionalInformation) {
            this.pixels = floats;
            this.additionalInformation = additionalInformation;
        }
    }
    public static class Output {
        public final Hero hero;
        public final HeroState state;

        public Output(Hero hero, HeroState state) {
            this.hero = hero;
            this.state = state;
        }
        public String toString() {
            return "{" + hero.display + ", " + state.name() + "}";
        }
    }

    static Input getInput(BufferedImage image) {
        return getInput(image, Registry.getInstance().random.nextInt(Integer.MAX_VALUE));
    }
    static Input getInput(BufferedImage image, int imageCounter) {
        BufferedImage scaledImage = ImageUtils.scale(image);
        try {
            ImageIO.write(scaledImage, "png", new File("tst/foo_" + String.valueOf(imageCounter) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Input(ImageUtils.getFloats(scaledImage), getAdditionalInformation(scaledImage));
    }


    private static float[] getAdditionalInformation(BufferedImage image) {
        int[] colorsOfInterest = Registry.getInstance().config.colorsOfInterest;
        int[] ints = ImageUtils.countPixelsInImage(colorsOfInterest, image);
        double[] avg = ImageUtils.averageImage(image);


        float[] ret = new float[ints.length + avg.length];
        if (ret.length != getAdditionalDimension()) {
            throw new IllegalStateException("Invalid value returned for the number of additional features");
        }

        int k = 0;
        for (int i=0;i<ints.length;i++) {
            ret[k++] = ints[i];
        }
        for (int i=0;i<avg.length;i++) {
            ret[k++] = (float) avg[i];
        }

//        for (int i=0;i<ret.length;i++) {
//            System.out.print(ret[i]);
//            System.out.print(" ");
//        }
//        System.out.println();

        return ret;
    }

    static final int NUMBER_OF_STATES = 5;
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
                return HeroState.Unavailable;
            default:
                throw new IllegalArgumentException("Cannot train data to be " + idx);
        }
    }

    private static int getMaxIdx(float[] array, int start, int stop) {
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

    static int[] getY(Hero hero, HeroState state) {
        int num = Registry.getInstance().heroes.getMaximumIndex();
        int[] returnValue = new int[getYDimension()];
        returnValue[hero.getIndex()] = 1;
        returnValue[num + 1 + getHeroStateArrayIndex(state)] = 1;


        float[] arr = new float[returnValue.length];
        for (int i=0;i<returnValue.length;i++) {
            arr[i] = returnValue[i];
        }

        Output o = parseY(arr);
        if (o.hero.display != hero.display || !state.equals(o.state)) {
            throw new IllegalStateException("Hero does not map back to what it should: " + hero.display + " " + state.name() + ": " + o);
        }

        return returnValue;
    }

    static Output parseY(float[] array) {
        int heroesLength = Registry.getInstance().heroes.getMaximumIndex();
        int heroIndex = getMaxIdx(array, 0, heroesLength + 1);
        int stateIndex = getMaxIdx(array, heroesLength + 1, array.length);
        Hero hero = Registry.getInstance().heroes.getHeroByIndex(heroIndex);
        HeroState state = getHeroStateFromIndex(stateIndex);
        return new Output(hero, state);
    }

    public static int getYDimension() {
        return Registry.getInstance().heroes.getMaximumIndex() + 1 + NUMBER_OF_STATES;
    }

    public static int getAdditionalDimension() {
        return Registry.getInstance().config.colorsOfInterest.length + 3;
    }
}
