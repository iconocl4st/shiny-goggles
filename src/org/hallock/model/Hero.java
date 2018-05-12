package org.hallock.model;

public class Hero {
    String id;
    String display;
    int pickId;

    public Hero(String id, Integer pickerId, String display) {
        this.id = id;
        this.pickId = pickerId;
        this.display = display;
    }
}
