package org.hallock.model;

import org.hallock.control.ApplicationContext;
import org.hallock.model.geometry.HeroGridGeometry;
import org.hallock.model.geometry.ImageRowGeometry;
import org.hallock.util.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
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
