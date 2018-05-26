package org.hallock.dota.nn;

import org.tensorflow.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

public class ImageClassifier {
    public static Features.Output[] classifyImages(BufferedImage[] images) {
        try (
                SavedModelBundle serve = SavedModelBundle.load("/work/dota/AutoPicker/python/saved_model", "serve");
                Graph graph = serve.graph();
                Session session = serve.session();
                PrintStream log = new PrintStream(new File("classification_results.txt"));
        ) {

            float[][][][] pixels = new float[images.length][][][];
            float[][] features = new float[images.length][];

            for (int i=0;i<images.length;i++) {
                Features.Input input = Features.getInput(images[i]);
                pixels[i] = input.pixels;
                features[i] = input.additionalInformation;
            }

            Session.Runner runner = session.runner();
            runner.feed("images_placeholder", Tensor.create(pixels));
            runner.feed("feature_placeholder", Tensor.create(features));

            runner.fetch("classified/concat");
            List<Tensor<?>> run = runner.run();

            float[][] predicted = new float[images.length][Features.getYDimension()];
            for (Tensor t : run) {
                // only one
                t.copyTo(predicted);
            }
            for (int i=0;i<predicted.length;i++) {
                for (int j=0;j<predicted[i].length;j++) {
                    log.print(predicted[i][j]);
                    log.print(" ");
                }
                log.println();
            }

            Features.Output[] returnValue = new Features.Output[images.length];
            for (int i=0;i<returnValue.length;i++) {
                returnValue[i] = Features.parseY(predicted[i]);
            }
            return returnValue;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
