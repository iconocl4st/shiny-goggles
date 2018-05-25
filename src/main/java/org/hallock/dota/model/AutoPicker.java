
package org.hallock.dota.model;

import org.hallock.dota.control.Registry;

import java.util.LinkedList;

public class AutoPicker {

    private LinkedList<StateIdentifier> identifiers;

    public void setIdentifiers(LinkedList<StateIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Identifications identifyPicks() {
        IdentificationContext context = new IdentificationContext();
        Identifications results = Identifications.createEmptyResults();
        for (StateIdentifier identifier : identifiers) {
            identifier.identify(context, Registry.getInstance().camera, results);
        }
        return results;
    }
}
