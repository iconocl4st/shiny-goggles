package org.hallock.dota.control;

import org.hallock.dota.model.*;
import org.hallock.dota.model.geometry.HeroGridGeometry;
import org.hallock.dota.model.geometry.ImageRowGeometry;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

public class AutoPickerBuilder {

    private static void addHeroGridIdentifiers(
            LinkedList<HeroIdentifier> identifiers,
            HeroGridGeometry grid,
            JSONObject gridConfig
    ) {
        int currentX = grid.heroStartX;
        int currentY = grid.heroStartY;
        JSONArray heroTypes = gridConfig.getJSONArray('types');
        for (int type=0; type<heroTypes.length(); type++) {
            JSONArray rows = heroTypes.getJSONArray(type);
            for (int r = 0; r < rows.length(); r++) {
                JSONArray heros = rows.getJSONArray(r);
                for (int h = 0; h < heros.length(); h++) {
                    JSONObject hero = heros.getJSONObject(h);
                    Rectangle location = new Rectangle(currentX, currentY, grid.heroWidth, grid.heroHeight);
                    identifiers.addLast(new HeroIdentifier(
                            location,
                            hero,
                            new HeroIdentification[]{
                                    new HeroIdentification(
                                            HeroState.Banned,
                                            new BufferedImage[]{getImage(hero.getString("banned-image"))}
                                    ),
                                    new HeroIdentification(
                                            HeroState.Available,
                                            new BufferedImage[]{getImage(hero.getString("available-image"))}
                                    ),
                                    new HeroIdentification(
                                            HeroState.Picked,
                                            new BufferedImage[]{getImage(hero.getString("unavailable-image"))}
                                    )
                            }
                    ));

                    currentX += grid.heroWidth + grid.heroHorizontalGap;
                }

                currentX = grid.heroStartX;
                currentY += grid.heroHeight + grid.heroVerticalGap;
            }
            currentY += grid.heroTypeGap - grid.heroVerticalGap;
        }
    }


    private static void addHeroPickIdentifiers(
            LinkedList<HeroIdentifier> identifiers,
            ImageRowGeometry grid
    ) {

    }


    private static void addPlayerIdentifier(
            LinkedList<HeroIdentifier> identifiers,
            ImageRowGeometry grid
    ) {

    }

    public static AutoPicker buildAutoPicker() {
        Config config = ApplicationContext.getInstance().config;

        config.heroConfigFile
        HashMap<String, Hero> herosById = null;

        HeroGridGeometry grid = new HeroGridGeometry();
        ImageRowGeometry pickedHeros = null;
        ImageRowGeometry playerNames = null;

        LinkedList<HeroIdentifier> identifiers = new LinkedList<>();

        JSONObject gridConfig = null;
        JSONObject userConfig = null;

        addHeroGridIdentifiers(identifiers, grid, gridConfig);
        addHeroPickIdentifiers(identifiers, pickedHeros);
        addPlayerIdentifier(identifiers, playerNames, userConfig);
    }

    // Should use the image directory from within the config.
    private static BufferedImage getImage(String location){
        String imageDirectory = ApplicationContext.getInstance().config.imageDirectory;
        try (InputStream inputStream = Files.newInputStream(Paths.get(imageDirectory, location))) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            ApplicationContext.getInstance().logger.log("Unable to load image at location " + location, e);
            return null;
        }
    }
}
