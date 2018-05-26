package org.hallock.dota.control.others;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Hero;
import org.hallock.dota.model.HeroState;
import org.hallock.dota.nn.Features;
import org.hallock.dota.nn.ImageClassifier;
import org.hallock.dota.nn.TrainingSetCreator;
import org.hallock.dota.util.StringUtils;
import org.json.JSONException;
import org.tensorflow.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TestSimpleNN {

    public static void updateTrainingSet() throws IOException {
        JsonFactory factory = new JsonFactory();
        try (
                JsonGenerator xsGenerator = factory.createGenerator(new File("./python/data/training_set_x.json"), JsonEncoding.UTF8);
                JsonGenerator ysGenerator = factory.createGenerator(new File("./python/data/training_set_y.json"), JsonEncoding.UTF8);
                JsonGenerator asGenerator = factory.createGenerator(new File("./python/data/training_set_a.json"), JsonEncoding.UTF8);
        ) {
            TrainingSetCreator.streamTrainingSet(xsGenerator, ysGenerator, asGenerator);
        }
    }

    public static void printLayerNames() throws JSONException, IOException {
        try (
                SavedModelBundle serve = SavedModelBundle.load("/work/dota/AutoPicker/python/saved_model", "serve");
                Graph graph = serve.graph();
                Session session = serve.session();
        ) {

            Iterator<Operation> operations = graph.operations();
            while (operations.hasNext()) {
                String name = operations.next().name();
                if (!name.contains("classified")) {
                    continue;
                }
                System.out.println(name);
            }
        }
    }

    public static void runSimpleNN() throws JSONException, IOException {
        LinkedList<BufferedImage> images = new LinkedList<>();
        LinkedList<HeroState> states = new LinkedList<>();
        LinkedList<Hero> heros = new LinkedList<>();

        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            for (Hero.ImageInformation imageInformation : hero.getCache()) {
                images.addLast(imageInformation.image);
                states.addLast(imageInformation.state);
                heros.addLast(hero);
            }
        }
        BufferedImage[] imageArray = images.toArray(new BufferedImage[0]);
        HeroState[] stateArray = states.toArray(new HeroState[0]);
        Hero[] heroeArray = heros.toArray(new Hero[0]);

        Features.Output[] outputs = ImageClassifier.classifyImages(imageArray);

        for (int i=0;i<outputs.length;i++) {
            System.out.println(
                    StringUtils.padRight(heroeArray[i].display, ' ', 20) + "\t" +
                    StringUtils.padRight(stateArray[i].name(), ' ', 20) + ": " +
                    outputs[i]
            );
        }
    }
}
