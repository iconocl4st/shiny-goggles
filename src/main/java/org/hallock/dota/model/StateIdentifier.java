package org.hallock.dota.model;

import org.hallock.dota.util.Cameras.Camera;

public interface StateIdentifier {
    public void identify(IdentificationContext context, Camera camera, Identifications pickedState);
}
