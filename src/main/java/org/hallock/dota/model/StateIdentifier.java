package org.hallock.dota.model;

import org.hallock.dota.util.Camera;

public interface StateIdentifier {
    public void identify(Camera camera, IdentificationResults pickedState);
}
