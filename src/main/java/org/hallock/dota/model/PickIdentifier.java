package org.hallock.dota.model;

import org.hallock.dota.util.Cameras.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PickIdentifier extends HeroIdentifier {
    Team team;

    public PickIdentifier(Rectangle rec, HeroIdentification[] possibleStates, Team team) {
        super(rec, possibleStates);
        this.team = team;
    }

    public HeroState getPickedState() {
        return team.equals(Team.RADIANT) ? HeroState.PickedRadiant : HeroState.PickedDire;
    }

    public void identify(IdentificationContext context, Camera camera, Identifications state) {
        BufferedImage observed = camera.shoot(location);
        HeroIdentification identification = getMinimumIdentificationCost(observed);
        if (identification == null) {
            state.couldNotIdentify(observed, null, getPickedState());
            return;
        }
        if (identification.hero.id.equals("none")) {
            return;
        }
        state.heroIdentified(identification.hero, getPickedState());
    }
}
