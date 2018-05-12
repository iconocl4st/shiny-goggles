package org.hallock.model;

import org.hallock.util.Camera;

public interface StateIdentifier {
    public void identify(Camera camera, Identifications pickedState);
}
