package org.hallock.dota.control;

import org.hallock.dota.model.*;
import org.hallock.dota.model.geometry.GridEnumerator;
import org.hallock.dota.model.geometry.HeroGridGeometry;
import org.hallock.dota.model.geometry.ImageRowGeometry;
import org.json.JSONArray;
import org.json.JSONException;
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
            final LinkedList<StateIdentifier> identifiers,
            HeroGridGeometry grid,
            JSONObject gridConfig
    ) throws JSONException {
        GridEnumerator.enumerateGrid(grid, gridConfig, new GridEnumerator.GridItemVisitor() {
            @Override
            public void visit(JSONObject config, Rectangle location) throws JSONException {
                Hero hero = Registry.getInstance().heroes.getHero(config.getString("hero"));

                LinkedList<HeroIdentification> list = new LinkedList<>();
                if (config.has("banned-images")) {
                    list.add(new HeroIdentification(
                            HeroState.Banned,
                            new BufferedImage[]{getImage(config.getString("banned-image"))}
                    ));
                }
                if (config.has("available-images")) {
                    list.add(new HeroIdentification(
                            HeroState.Available,
                            new BufferedImage[]{getImage(config.getString("available-image"))}
                    ));
                }
                if (config.has("unavailable-images")) {
                    list.add(new HeroIdentification(
                            HeroState.Picked,
                            new BufferedImage[]{getImage(config.getString("unavailable-image"))}
                    ));
                }
                identifiers.addLast(new HeroIdentifier(
                        location,
                        hero,
                        list.toArray(new HeroIdentification[0])
                ));
            }
        });
    }


    private static void addHeroPickIdentifiers(
            LinkedList<StateIdentifier> identifiers,
            ImageRowGeometry grid
    ) throws JSONException {
//        JSONArray radiantArray = pickConfig.getJSONArray("radiant");
//        for (int i=0;i<radiantArray.length(); i++) {
//            JSONObject heroObj = radiantArray.getJSONObject(i);
//            Hero hero = Registry.getInstance().heroes.getHero(heroObj.getString("hero"));
//            Rectangle location = new Rectangle();
//            identifiers.add(new HeroIdentifier(
//                    location,
//                    hero,
//                    new HeroIdentification[]{
//                            new HeroIdentification(
//                                    HeroState.PickedRadiant,
//                                    new BufferedImage[]{getImage(heroObj.getString("picked-image"))}
//                        )
//                    }
//            ));
//        }
//        JSONArray direArray = pickConfig.getJSONArray("dire");
//        for (int i=0;i<direArray.length(); i++) {
//            JSONObject heroObj = radiantArray.getJSONObject(i);
//            Hero hero = Registry.getInstance().heroes.getHero(heroObj.getString("hero"));
//            Rectangle location = new Rectangle();
//            identifiers.add(new HeroIdentifier(
//                    location,
//                    hero,
//                    new HeroIdentification[] {
//                            new HeroIdentification(
//                                    HeroState.PickedDire,
//                                    new BufferedImage[]{getImage(heroObj.getString("picked-image"))}
//                            )
//                    }
//            ));
//        }
    }


    private static void addPlayerIdentifier(
            LinkedList<StateIdentifier> identifiers,
            ImageRowGeometry grid
    ) {

    }

    public static LinkedList<StateIdentifier> builIdentifiers() throws JSONException {
        LinkedList<StateIdentifier> identifiers = new LinkedList<>();

        addHeroGridIdentifiers(
                identifiers,
                Registry.getInstance().gridGeometry,
                Registry.getInstance().gridConfig
        );

        addHeroPickIdentifiers(
                identifiers,
                Registry.getInstance().pickedGeometry
        );

        addPlayerIdentifier(
                identifiers,
                Registry.getInstance().nameGeometry
        );

        return identifiers;
    }

    public static AutoPicker buildAutoPicker(JSONObject layout)  throws JSONException {
        Registry.getInstance().gridConfig = layout.getJSONObject("grid").getJSONObject("config");

        Registry.getInstance().gridGeometry = HeroGridGeometry.parseHeroGridGeometry(
                layout.getJSONObject("grid").getJSONObject("geometry")
        );
        Registry.getInstance().pickedGeometry = ImageRowGeometry.parseGeometry(
                layout.getJSONObject("picks").getJSONObject("geometry")
        );
        Registry.getInstance().nameGeometry = ImageRowGeometry.parseGeometry(
                layout.getJSONObject("players").getJSONObject("geometry")
        );

        AutoPicker picker = new AutoPicker();
        picker.setIdentifiers(builIdentifiers());
        return picker;
    }

    // Should use the image directory from within the config.
    private static BufferedImage getImage(String location){
        String imageDirectory = Registry.getInstance().config.imageDirectory;
        try (InputStream inputStream = Files.newInputStream(Paths.get(imageDirectory, location))) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            Registry.getInstance().logger.log("Unable to load image at location " + location, e);
            return null;
        }
    }
}
