package org.hallock.dota.model;

import org.hallock.dota.control.Registry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class IdentificationContext {
    private final HashMap<String, Hero> unavailableHeroes = new HashMap<>();
    private final HashMap<String, BufferedImage> previousImages = new HashMap<>();
    private final LinkedList<Point> areTheSameLocations = buildAreTheSameLocations();

    void addUnavailableHero(Hero hero) {
        unavailableHeroes.put(hero.id, hero);
    }

    Collection<Hero> getUnavailableHeroes() {
        return unavailableHeroes.values();
    }

    boolean hasImageChanged(String key, BufferedImage image) {
        BufferedImage previousImage = previousImages.get(key);
        for (Point p : areTheSameLocations) {
            int previousRgb = previousImage.getRGB(p.x, p.y);
            int currentRgb = image.getRGB(p.x, p.y);
            if (previousRgb != currentRgb) {
                return true;
            }
        }
        return false;
    }

    private static final LinkedList<Point> buildAreTheSameLocations() {
        LinkedList<Point> points = new LinkedList<>();
        for (int i=0;i<100;i++) {
            int x = Registry.getInstance().random.nextInt(
                    Registry.getInstance().config.IMAGE_WIDTH
            );
            int y = Registry.getInstance().random.nextInt(
                    Registry.getInstance().config.IMAGE_HEIGHT
            );
            points.addLast(new Point(x, y));
        }
        return points;
    }
}
