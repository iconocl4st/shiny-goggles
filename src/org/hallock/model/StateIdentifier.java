package org.hallock.model;

import org.hallock.util.Camera;

public interface StateIdentifier {
    public void identify(IdentificationContext context, Camera camera, Identifications pickedState);
}
