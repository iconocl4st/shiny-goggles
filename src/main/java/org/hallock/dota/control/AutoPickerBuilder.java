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
import java.util.LinkedList;

public class AutoPickerBuilder {

    private static void addHeroGridIdentifiers(
            LinkedList<StateIdentifier> identifiers,
            HeroGridGeometry grid,
            JSONObject gridConfig
    ) {
        int currentX = grid.heroStartX;
        int currentY = grid.heroStartY;
        JSONArray heroTypes = gridConfig.getJSONArray("types");
        for (int type=0; type<heroTypes.length(); type++) {
            JSONArray rows = heroTypes.getJSONArray(type);
            for (int r = 0; r < rows.length(); r++) {
                JSONArray heros = rows.getJSONArray(r);
                for (int h = 0; h < heros.length(); h++) {
                    JSONObject spec = heros.getJSONObject(h);
                    Hero hero = ApplicationContext.getInstance().heroes.getHero(spec.getString("hero"));
                    Rectangle location = new Rectangle(currentX, currentY, grid.heroWidth, grid.heroHeight);
                    identifiers.addLast(new HeroIdentifier(
                            location,
                            hero,
                            new HeroIdentification[]{
                                    new HeroIdentification(
                                            HeroState.Banned,
                                            new BufferedImage[]{getImage(spec.getString("banned-image"))}
                                    ),
                                    new HeroIdentification(
                                            HeroState.Available,
                                            new BufferedImage[]{getImage(spec.getString("available-image"))}
                                    ),
                                    new HeroIdentification(
                                            HeroState.Picked,
                                            new BufferedImage[]{getImage(spec.getString("unavailable-image"))}
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
            LinkedList<StateIdentifier> identifiers,
            ImageRowGeometry grid,
            JSONObject pickConfig
    ) {
        JSONArray radiantArray = pickConfig.getJSONArray("radiant");
        for (int i=0;i<radiantArray.length(); i++) {
            JSONObject heroObj = radiantArray.getJSONObject(i);
            Hero hero = ApplicationContext.getInstance().heroes.getHero(heroObj.getString("hero"));
            Rectangle location = new Rectangle();
            identifiers.add(new HeroIdentifier(
                    location,
                    hero,
                    new HeroIdentification[]{
                            new HeroIdentification(
                                    HeroState.PickedRadiant,
                                    new BufferedImage[]{getImage(heroObj.getString("picked-image"))}
                        )
                    }
            ));
        }
        JSONArray direArray = pickConfig.getJSONArray("dire");
        for (int i=0;i<direArray.length(); i++) {
            JSONObject heroObj = radiantArray.getJSONObject(i);
            Hero hero = ApplicationContext.getInstance().heroes.getHero(heroObj.getString("hero"));
            Rectangle location = new Rectangle();
            identifiers.add(new HeroIdentifier(
                    location,
                    hero,
                    new HeroIdentification[] {
                            new HeroIdentification(
                                    HeroState.PickedDire,
                                    new BufferedImage[]{getImage(heroObj.getString("picked-image"))}
                            )
                    }
            ));
        }
    }


    private static void addPlayerIdentifier(
            LinkedList<StateIdentifier> identifiers,
            ImageRowGeometry grid,
            JSONObject playerConfig
    ) {

    }

    public static AutoPicker buildAutoPicker(JSONObject layout) throws IOException {
        HeroGridGeometry grid = new HeroGridGeometry();
        ImageRowGeometry pickedHeros = new ImageRowGeometry();
        ImageRowGeometry playerNames = new ImageRowGeometry();
        LinkedList<StateIdentifier> identifiers = new LinkedList<>();

        JSONObject gridObj = layout.getJSONObject("grid");
        addHeroGridIdentifiers(
                identifiers,
                HeroGridGeometry.parseHeroGridGeometry(gridObj.getJSONObject("geometry")),
                gridObj.getJSONObject("config")
        );

        JSONObject picks = layout.getJSONObject("picks");
        addHeroPickIdentifiers(
                identifiers,
                ImageRowGeometry.parseGeometry(picks.getJSONObject("geometry")),
                picks.getJSONObject("config")
        );

        JSONObject players = layout.getJSONObject("players");
        addPlayerIdentifier(
                identifiers,
                ImageRowGeometry.parseGeometry(players.getJSONObject("geometry")),
                players.getJSONObject("config")
        );
        return new AutoPicker(identifiers);
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
