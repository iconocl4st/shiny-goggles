package org.hallock.dota.model;

import org.hallock.dota.control.ApplicationContext;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.hallock.dota.util.OneIdea;
import org.hallock.dota.util.Camera;
import org.json.JSONObject;

public class HeroIdentifier implements StateIdentifier {
    Rectangle location;
    Hero hero;
    HeroIdentification[] possibleStates;

    HeroIdentifier(Rectangle rec, Hero hero, HeroIdentification[] possibleStates) {
        this.location = rec;
        this.hero = hero;
        this.possibleStates = possibleStates;
    }


    void HeroIdentifier(JSONObject config) {

    }

    void getMinimumIdentificationCost(BufferedImage observed) {
        // Could perform some check for change here if it is more efficient.

        double minimumDistance = Double.POSITIVE_INFINITY;
        HeroIdentification minimum = null;
        for (HeroIdentification identification : possibleStates) {
            for (BufferedImage image : identification.images) {
                double distance = OneIdea.getDistance(
                        image,
                        observed,
                        ApplicationContext.getInstance().config.comparisonParameter
                );
                if (distance > minimumDistance) {
                    continue;
                }
                minimumDistance = distance;
                minimum = identification;
            }
        }
        return minimum;
    }

    void identify(Camera camera, IdentificationResults state) {
        HeroIdentification minimum = getMinimumIdentificationCost(camera.shoot(location));
        if (minimum == null) {
            throw new Exception('It is null');
        }
        state.heroIdentified(hero, minimum.state);
    }
}
