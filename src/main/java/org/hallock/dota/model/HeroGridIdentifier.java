package org.hallock.dota.model;

import org.hallock.dota.util.Cameras;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroGridIdentifier extends HeroIdentifier {
    Hero hero;

    public HeroGridIdentifier(Rectangle rec, HeroIdentification[] possibleStates, Hero hero) {
        super(rec, possibleStates);
        this.hero = hero;
    }

    public void identify(Cameras.Camera camera, Identifications state) {
        BufferedImage observed = camera.shoot(location);
        HeroIdentification identification = getMinimumIdentificationCost(observed);
        if (identification != null) {
            state.heroIdentified(identification.hero, identification.state);
            return;
        }
        state.heroIdentified(hero, HeroState.Unidentified);
        state.couldNotIdentify(observed, hero, null);
    }
}
