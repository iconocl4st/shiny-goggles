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
                list.add(new HeroIdentification(
                        hero,
                        HeroState.Banned,
                        hero.getImagesByType(HeroState.Banned)
                ));
                list.add(new HeroIdentification(
                        hero,
                        HeroState.Available,
                        hero.getImagesByType(HeroState.Available)
                ));
                list.add(new HeroIdentification(
                        hero,
                        HeroState.Unavailable,
                        hero.getImagesByType(HeroState.Unavailable)
                ));
                identifiers.addLast(new HeroIdentifier(
                        location,
                        list.toArray(new HeroIdentification[0])
                ));
            }
        });
    }


    private static void addHeroPickIdentifiers(
            LinkedList<StateIdentifier> identifiers,
            ImageRowGeometry grid
    ) throws JSONException {
        final Heroes heroes = Registry.getInstance().heroes;
        GridEnumerator.enumerateGrid(
                grid,
                new GridEnumerator.RowItemVisitor() {
                    @Override
                    public void visit(Rectangle location, Team team, int idx) {
                        LinkedList<HeroIdentification> identifications = new LinkedList<>();
                        HeroState picked = team.equals(Team.RADIANT) ? HeroState.PickedRadiant : HeroState.PickedDire;
                        for (Hero hero : heroes.getAll()) {
                            identifications.add(new HeroIdentification(
                                    hero,
                                    picked,
                                    hero.getImagesByType(HeroState.Picked)
                            ));
                        }
                        identifiers.add(new HeroIdentifier(location, identifications.toArray(new HeroIdentification[0])));
                    }
                }
        );
    }


    private static void addPlayerIdentifier(
            final LinkedList<StateIdentifier> identifiers,
            ImageRowGeometry grid
    ) {
        GridEnumerator.enumerateGrid(
                grid,
                new GridEnumerator.RowItemVisitor() {
                    @Override
                    public void visit(Rectangle location, Team team, int idx) {
                        identifiers.add(new UserIdentifier(team, location, idx));
                    }
                }
        );
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
