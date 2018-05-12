package org.hallock.model;

import java.awt.image.BufferedImage;

public class HeroIdentification {
    HeroState state;
    BufferedImage[] images;

    public HeroIdentification(HeroState state, BufferedImage[] images) {
        this.state = state;
        this.images = images;
    }
}

