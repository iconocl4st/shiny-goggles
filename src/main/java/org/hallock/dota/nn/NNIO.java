package org.hallock.dota.nn;

import com.fasterxml.jackson.core.JsonGenerator;
import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Hero;
import org.hallock.dota.model.HeroState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NNIO {

    public  static void streamTrainingSet(JsonGenerator generator) throws IOException {
        generator.writeStartObject();

        generator.writeFieldName("x");
        generator.writeStartArray();
        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            System.out.println("writing xs for " + hero.id);
            for (Hero.ImageInformation info : hero.getCache()) {
                BufferedImage scaledImage = ImageUtils.scale(info.image);
                try {
                    ImageIO.write(scaledImage, "png", new File("tst/foo_" + String.valueOf(Math.random()) + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                float[][][] x = ImageUtils.getFloats(scaledImage);
                generator.writeStartArray();
                for (int i=0;i<x.length;i++) {
                    generator.writeStartArray();
                    for (int j=0;j<x[i].length;j++) {
                        generator.writeStartArray();
                        for (int k=0;k<x[i][j].length;k++) {
                            generator.writeNumber(x[i][j][k]);
                        }
                        generator.writeEndArray();
                    }
                    generator.writeEndArray();
                }
                generator.writeEndArray();
            }
        }
        generator.writeEndArray();



        generator.writeFieldName("y");
        generator.writeStartArray();
        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            System.out.println("writing ys for " + hero.id);
            for (Hero.ImageInformation info : hero.getCache()) {
                int[] y = getY(hero, info.state);
                generator.writeStartArray();
                for (int i = 0; i < y.length; i++) {
                    generator.writeNumber(y[i]);
                }
                generator.writeEndArray();
            }
        }
        generator.writeEndArray();


        generator.writeFieldName("additional");
        generator.writeStartArray();
        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            System.out.println("writing additional for " + hero.id);
            for (Hero.ImageInformation info : hero.getCache()) {
                double[] z = getAdditionalInformation(info.image);
                generator.writeStartArray();
                for (int i=0;i<z.length;i++) {
                    generator.writeNumber(z[i]);
                }
                generator.writeEndArray();
            }
        }
        generator.writeEndArray();
        generator.writeEndObject();
    }


    public static JSONObject getTrainingSet() throws JSONException {
        JSONArray trainingX = new JSONArray();
        JSONArray trainingY = new JSONArray();
        JSONArray additional = new JSONArray();


        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            System.out.println("Serializing " + hero.id);
            for (Hero.ImageInformation info : hero.getCache()) {
                int[] y = getY(hero, info.state);
                JSONArray ys = new JSONArray();
                for (int i=0;i<y.length;i++) {
                    ys.put(y[i]);
                }
                trainingY.put(ys);


                double[] z = getAdditionalInformation(info.image);
                JSONArray zs = new JSONArray();
                for (int i=0;i<z.length;i++) {
                    zs.put(z[i]);
                }
                additional.put(zs);


                BufferedImage scaledImage = ImageUtils.scale(info.image);
                try {
                    ImageIO.write(scaledImage, "png", new File("tst/foo_" + String.valueOf(Math.random()) + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                float[][][] x = ImageUtils.getFloats(scaledImage);
                JSONArray w = new JSONArray();
                for (int i=0;i<x.length;i++) {
                    JSONArray h = new JSONArray();
                    for (int j=0;j<x[i].length;j++) {
                        JSONArray p = new JSONArray();
                        for (int k=0;k<x[i][j].length;k++) {
                            p.put(x[i][j][k]);
                        }
                        h.put(p);
                    }
                    w.put(h);
                }
                trainingX.put(w);
            }
        }

        JSONObject ret = new JSONObject();
        ret.put("x", trainingX);
        ret.put("y", trainingY);
        ret.put("additional", additional);
        return ret;
    }

    private static double[] getAdditionalInformation(BufferedImage image) {
        int[] colorsOfInterest = Registry.getInstance().config.colorsOfInterest;
        int[] ints = ImageUtils.countPixelsInImage(colorsOfInterest, image);
        double[] avg = ImageUtils.averageImage(image);


        double[] ret = new double[ints.length + avg.length];

        int k = 0;
        for (int i=0;i<ints.length;i++) {
            ret[k++] = ints[i];
        }
        for (int i=0;i<avg.length;i++) {
            ret[k++] = avg[i];
        }

        for (int i=0;i<ret.length;i++) {
            System.out.print(ret[i]);
            System.out.print(" ");
        }
        System.out.println();

        return ret;
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
                return HeroState.Unavailable;
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
        int num = Registry.getInstance().heroes.getMaximumIndex();
        int[] returnValue = new int[
                num + 5
        ];
        returnValue[hero.getIndex()] = 1;
        returnValue[num + getHeroStateArrayIndex(state)] = 1;
        return returnValue;
    }
}
