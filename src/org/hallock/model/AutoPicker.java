package org.hallock.model;

import org.hallock.util.Camera;

import java.util.LinkedList;

public class AutoPicker {

    // first player [x=168,y=10,width=130,height=77]
    // first hero [x=172,y=188,width=62,height=89]

    private final Camera camera;
    private final LinkedList<StateIdentifier> identifiers;
    
    public AutoPicker(Camera camera, LinkedList<StateIdentifier> identifiers) {
        this.camera = camera;
        this.identifiers = identifiers;
    }


    public Identifications identifyPicks() {
        Identifications results = new Identifications();
        for (StateIdentifier identifier : identifiers) {
            identifier.identify(this.camera, results);
        }
        return results;
    }
}
