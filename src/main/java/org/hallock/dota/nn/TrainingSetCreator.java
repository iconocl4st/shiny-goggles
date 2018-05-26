package org.hallock.dota.nn;

import com.fasterxml.jackson.core.JsonGenerator;
import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Hero;
import org.hallock.dota.model.HeroState;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class TrainingSetCreator {

    public static void streamTrainingSet(
            JsonGenerator xsGenerator,
            JsonGenerator ysGenerator,
            JsonGenerator asGenerator

    ) throws IOException {
        xsGenerator.writeStartObject();
        xsGenerator.writeFieldName("image-width");
        xsGenerator.writeNumber(Registry.getInstance().config.IMAGE_WIDTH);
        xsGenerator.writeFieldName("image-height");
        xsGenerator.writeNumber(Registry.getInstance().config.IMAGE_HEIGHT);
        xsGenerator.writeFieldName("array");
        xsGenerator.writeStartArray();

        ysGenerator.writeStartObject();
        ysGenerator.writeFieldName("y-dim");
        ysGenerator.writeNumber(Features.getYDimension());
        ysGenerator.writeFieldName("hero-dim");
        ysGenerator.writeNumber(Registry.getInstance().heroes.getMaximumIndex() + 1);
        ysGenerator.writeFieldName("state-dim");
        ysGenerator.writeNumber(Features.NUMBER_OF_STATES);
        ysGenerator.writeFieldName("array");
        ysGenerator.writeStartArray();

        asGenerator.writeStartObject();
        asGenerator.writeFieldName("additional-dim");
        asGenerator.writeNumber(Features.NUMBER_OF_STATES);
        asGenerator.writeFieldName("array");
        asGenerator.writeStartArray();

        int imageCounter = 0;
        int heroCount = 0;

        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            if (false && ++heroCount > 2) {
                continue;
            }
            if (hero.display.equals("None")) {
                continue;
            }
            System.out.println("writing " + hero.id);
            for (Hero.ImageInformation info : hero.getCache()) {
                write_training_point(xsGenerator, ysGenerator, asGenerator, hero, info.image, info.state, imageCounter++);
                for (int i=0; i<0;i++) {
                    int amountToShake = 7;
                    int trimLft = Registry.getInstance().random.nextInt(amountToShake);
                    int trimRgt = Registry.getInstance().random.nextInt(amountToShake);
                    int trimTop = Registry.getInstance().random.nextInt(amountToShake);
                    int trimBot = Registry.getInstance().random.nextInt(amountToShake);


                    BufferedImage subimage = info.image.getSubimage(
                            trimLft,
                            trimTop,
                            info.image.getWidth() - trimLft - trimRgt,
                            info.image.getHeight() - trimTop - trimBot
                    );

                    write_training_point(xsGenerator, ysGenerator, asGenerator, hero, subimage, info.state, imageCounter++);
                }
            }
        }

        xsGenerator.writeEndArray();
        xsGenerator.writeEndObject();

        ysGenerator.writeEndArray();
        ysGenerator.writeEndObject();

        asGenerator.writeEndArray();
        asGenerator.writeEndObject();
    }

    private static void write_training_point(
            JsonGenerator xsGenerator,
            JsonGenerator ysGenerator,
            JsonGenerator asGenerator,
            Hero hero,
            BufferedImage image,
            HeroState state,
            int imageCounter
    ) throws IOException {
        Features.Input input = Features.getInput(image, imageCounter);

        // X-vector
        xsGenerator.writeStartArray();
        for (int i=0;i<input.pixels.length;i++) {
            xsGenerator.writeStartArray();
            for (int j=0;j<input.pixels[i].length;j++) {
                xsGenerator.writeStartArray();
                for (int k=0;k<input.pixels[i][j].length;k++) {
                    xsGenerator.writeNumber(input.pixels[i][j][k]);
                }
                xsGenerator.writeEndArray();
            }
            xsGenerator.writeEndArray();
        }
        xsGenerator.writeEndArray();

        // Y vector
        int[] y = Features.getY(hero, state);
        ysGenerator.writeStartArray();
        for (int i = 0; i < y.length; i++) {
            ysGenerator.writeNumber(y[i]);
        }
        ysGenerator.writeEndArray();

        // Additional information
        asGenerator.writeStartArray();
        for (int i=0;i<input.additionalInformation.length;i++) {
            asGenerator.writeNumber(input.additionalInformation[i]);
        }
        asGenerator.writeEndArray();
    }
}
