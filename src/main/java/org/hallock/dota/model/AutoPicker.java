
package org.hallock.dota.model;

import org.hallock.dota.control.Registry;

import java.util.LinkedList;

public class AutoPicker {

    private LinkedList<StateIdentifier> identifiers;

    public void setIdentifiers(LinkedList<StateIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Identifications.IdentificationResults identifyPicks() {
        Identifications results = Identifications.createEmptyResults();
        for (StateIdentifier identifier : identifiers) {
            identifier.identify(Registry.getInstance().camera, results);
        }
        Identifications.IdentificationResults picks = results.getResults();
        Registry.getInstance().ui.setPicks(picks, results.getUnidentified());
        return picks;
    }
}
