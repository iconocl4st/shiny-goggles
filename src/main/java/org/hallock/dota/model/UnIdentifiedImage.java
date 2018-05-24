package org.hallock.dota.model;

import org.hallock.dota.control.Registry;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class UnIdentifiedImage {
    public Hero hero;
    public BufferedImage image;
    public HeroState guessedState;

    UnIdentifiedImage(BufferedImage image, Hero hero, HeroState state) {
        this.hero = hero;
        this.image = image;
        this.guessedState = state;

        if (hero == null) {
            this.hero = Registry.getInstance().heroes.getHero("abaddon");
        }
        if (state == null) {
            this.guessedState = HeroState.Available;
        }
    }
    void setHero(Hero hero) {
        this.hero = hero;
    }

    void setState(HeroState state) {
        this.guessedState = state;
    }

    public void persist() throws IOException {
        if (hero == null) {
            throw new IllegalStateException("No hero");
        }
        if (image == null) {
            throw new IllegalStateException("No Image");
        }
        if (guessedState == null) {
            throw new IllegalStateException("No state chosen");
        }
        hero.addImage(guessedState, image);
    }
}
