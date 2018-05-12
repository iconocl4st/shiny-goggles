package org.hallock.dota.model;

import java.awt.image.BufferedImage;

public class HeroIdentification {
    HeroState state;
    BufferedImage[] images;

    HeroIdentification(HeroState state, BufferedImage[] images) {
        this.state = state;
        this.images = images;
    }
}

