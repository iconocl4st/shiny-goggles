package org.hallock.dota.model;

import org.hallock.dota.util.Camera;

import java.util.LinkedList;

public class AutoPicker {

    // first player [x=168,y=10,width=130,height=77]
    // first hero [x=172,y=188,width=62,height=89]

    private final Camera camera;
    private final LinkedList<HeroIdentifier> identifiers;
    
    public AutoPicker(Camera camera) {
        this.camera = camera;
        identifiers = new LinkedList<>();
    }

    public IdentificationResults identifyPicks() {
        return null;
    }

    public void update() {

    }

}
