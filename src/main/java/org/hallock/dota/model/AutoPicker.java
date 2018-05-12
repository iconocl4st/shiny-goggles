
package org.hallock.dota.model;

import org.hallock.dota.control.Registry;

import java.util.LinkedList;

public class AutoPicker {

    // first player [x=168,y=10,width=130,height=77]
    // first hero [x=172,y=188,width=62,height=89]

    private final LinkedList<StateIdentifier> identifiers;
    
    public AutoPicker(LinkedList<StateIdentifier> identifiers) {
        this.identifiers = identifiers;
    }


    public Identifications identifyPicks() {
        Identifications results = Identifications.createEmptyResults();
        for (StateIdentifier identifier : identifiers) {
            identifier.identify(Registry.getInstance().camera, results);
        }
        return results;
    }
}
