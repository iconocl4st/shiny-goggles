package org.hallock.model;

import java.util.LinkedList;

public class IdentificationResults {
    public LinkedList<Integer> direPicked;
    public LinkedList<Integer> radiantPicked;
    public LinkedList<Integer> direBanned;
    public LinkedList<Integer> radiantBanned;
    public LinkedList<Integer> unavailable;
    public boolean playerIsRadiant;

    public void heroIdentified(HeroIdentification identification) {

    }

    public void userIdentified(UserIdentification identification) {

    }
}