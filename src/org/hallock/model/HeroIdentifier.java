package org.hallock.model;

import org.hallock.model.HeroIdentification;

import java.awt.*;
import java.util.LinkedList;

import org.json.JSONObject;

public class HeroIdentifier {
    Rectangle location;
    String heroName;
    int heroId;
    LinkedList<HeroIdentification> possibleStates;



    void HeroIdentifier(JSONObject config) {

    }
}
