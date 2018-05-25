package org.hallock.dota.model;

import org.hallock.dota.control.Registry;
import org.hallock.dota.util.Cameras;
import org.hallock.dota.util.OneIdea;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class HeroIdentifier implements StateIdentifier {
    Rectangle location;
    HeroIdentification[] possibleStates;

    public HeroIdentifier(Rectangle rec, HeroIdentification[] possibleStates) {
        this.location = rec;
        this.possibleStates = possibleStates;
    }

    HeroIdentification getMinimumIdentificationCost(BufferedImage observed) {
        // Could perform some check for change here if it is more efficient.
        double minimumDistance = Double.POSITIVE_INFINITY;
        HeroIdentification minimum = null;
        for (HeroIdentification identification : possibleStates) {
            for (BufferedImage image : identification.images) {
                double distance = OneIdea.getDistance(
                        image,
                        observed,
                        Registry.getInstance().config.comparisonParameter
                );
                if (distance > 500) {
                    continue;
                }
                if (distance > minimumDistance) {
                    continue;
                }
                minimumDistance = distance;
                minimum = identification;
            }
        }
        return minimum;
    }

    public void identify(IdentificationContext context, Cameras.Camera camera, Identifications state) {
        HeroIdentification minimum = getMinimumIdentificationCost(camera.shoot(location));
        if (minimum == null) {
            // throw new Exception("It is null");
            System.out.println("It is null");
        }
        state.heroIdentified(minimum.hero, minimum.state);
//        if (context.hasImageChanged(context.))

        if (minimum.state == HeroState.Unavailable) {
            context.addUnavailableHero(minimum.hero);
        }
    }
}
