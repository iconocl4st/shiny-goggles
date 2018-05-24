package org.hallock.dota.model;

import org.hallock.dota.control.Config;
import org.hallock.dota.control.Registry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Identifications {
    private HashMap<Integer, HeroState> previous = new HashMap<>();
    private LinkedList<UnIdentifiedImage> unidentifiedImages = new LinkedList<>();
    private Team userTeam = null;

    private Identifications() {}

    private void setState(Hero hero, HeroState state) {
        previous.put(hero.pickId, state);
    }

    public void heroIdentified(Hero hero, HeroState state) {
        Registry.getInstance().logger.log(hero + " identified as " + state);

        int id = hero.pickId;
        HeroState previousState = previous.get(id);
        if (state.equals(HeroState.Unidentified)) {
//            throw new IllegalArgumentException("This happens");
        }
        if (previousState == null) {
            setState(hero, state);
            return;
        }
        switch (previousState) {
            case Banned:
                switch (state) {
                    case Unidentified:
                    case Banned:
                    case Unavailable:
                        break;
                    case PickedRadiant:
                    case Available:
                    case PickedDire:
                    case Picked:
                        throw new IllegalArgumentException("cannot go from " + previousState + " to " + state);
                }
                break;
            case Available:
                switch (state) {
                    case Unidentified:
                    case Available:
                        break;
                    case PickedDire:
                    case PickedRadiant:
                    case Picked:
                    case Banned:
                    case Unavailable:
                        throw new IllegalArgumentException("cannot go from " + previousState + " to " + state);
                }
                break;
            case PickedDire:
                switch (state) {
                    case PickedDire:
                    case Unavailable:
                    case Picked:
                    case Unidentified:
                        break;
                    case Available:
                    case Banned:
                    case PickedRadiant:
                        throw new IllegalArgumentException("cannot go from " + previousState + " to " + state);
                }
                break;
            case PickedRadiant:
                switch (state) {
                    case PickedRadiant:
                    case Unavailable:
                    case Unidentified:
                    case Picked:
                        break;
                    case Banned:
                    case Available:
                    case PickedDire:
                        throw new IllegalArgumentException("cannot go from " + previousState + " to " + state);
                }
                break;
            case Unavailable:
                switch (state) {
                    case Banned:
                    case Available:
                    case PickedDire:
                    case PickedRadiant:
                    case Unavailable:
                    case Picked:
                    case Unidentified:
                        setState(hero, state);
                }
                break;
            case Picked:
                switch (state) {
                    case Unidentified:
                    case Picked:
                        break;
                    case PickedDire:
                    case PickedRadiant:
                        setState(hero, state);
                        break;
                    case Banned:
                    case Available:
                    case Unavailable:
                        throw new IllegalArgumentException("cannot go from " + previousState + " to " + state);
                }
                break;
            case Unidentified:
                switch (state) {
                    case Banned:
                    case Available:
                    case PickedDire:
                    case PickedRadiant:
                    case Unavailable:
                    case Picked:
                    case Unidentified:
                        setState(hero, state);
                        break;
                }
                break;
        }
    }

    public void userIdentified(Team team) {
        if (userTeam == null) {
            userTeam = team;
            return;
        }
        if (userTeam.equals(team)) {
            return;
        }
        throw new IllegalArgumentException("User team already set to " + userTeam + ", but changed to " + team);
    }

    public IdentificationResults getResults() {
        IdentificationResults results = new IdentificationResults();

        for (Map.Entry<Integer, HeroState> entry : previous.entrySet()) {
            switch(entry.getValue()) {
                case Available:
                    break;
                case Banned:
                    results.banned.add(entry.getKey());
                    break;
                case PickedDire:
                    results.direPicked.add(entry.getKey());
                    break;
                case PickedRadiant:
                    results.radiantPicked.add(entry.getKey());
                    break;
                case Unavailable:
                    results.unavailable.add(entry.getKey());
                    break;
                case Picked:
                    throw new IllegalStateException("Found picked hero, but do not know which team: " + entry.getKey());
                case Unidentified:
                    // throw new IllegalStateException("Did not determine the state of hero: " + entry.getKey());
                    System.out.println("Did not determine the state of hero: " + entry.getKey());
                    break;
                default:
                    throw new IllegalStateException("I added a state with adding it to this switch statement.");
            }
        }

        if (userTeam == null) {
//            throw new IllegalStateException("Did not find the team for the user");
            System.out.println("Did not find the team for the user");
        } else {
            results.playerIsRadiant = userTeam.equals(Team.RADIANT);
        }

        return results;
    }

    public void couldNotIdentify(BufferedImage observed, Hero hero, HeroState state) {
        unidentifiedImages.add(new UnIdentifiedImage(observed, hero, state));
    }

    public LinkedList<UnIdentifiedImage> getUnidentified() {
        return (LinkedList<UnIdentifiedImage>) unidentifiedImages.clone();
    }

    public static class IdentificationResults {
        public LinkedList<Integer> direPicked = new LinkedList<>();
        public LinkedList<Integer> radiantPicked = new LinkedList<>();
        public LinkedList<Integer> banned = new LinkedList<>();
        public LinkedList<Integer> unavailable = new LinkedList<>();
        public Boolean playerIsRadiant = null;

        static JSONArray jsonArray(LinkedList<Integer> list) {
            JSONArray array = new JSONArray();
            for (Integer i : list) array.put(i);
            return array;
        }

        public JSONObject toJson() throws JSONException {
            if (playerIsRadiant == null) {
                throw new IllegalStateException("Player team not given!");
            }

            LinkedList<Integer> radiantBan = new LinkedList<>();
            LinkedList<Integer> direBan = new LinkedList<>();
            for (Integer i : banned) {
                if (radiantBan.size() >= Config.TEAM_LENGTH) {
                    direBan.add(i);
                } else {
                    radiantBan.add(i);
                }
            }


            JSONObject returnValue = new JSONObject();
            returnValue.put("ping", true);
            returnValue.put("isRadiant", (boolean) playerIsRadiant);
            returnValue.put("radiantPick", jsonArray(radiantPicked));
            returnValue.put("direPick", jsonArray(direPicked));
            returnValue.put("radiantBan", jsonArray(radiantBan));
            returnValue.put("direBan", jsonArray(direBan));
            returnValue.put("unavailableHeroes", jsonArray(unavailable));
            returnValue.put("app", Registry.getInstance().config.appName);
            return returnValue;
        }
    }

    public static Identifications createEmptyResults() {
        Identifications returnValue = new Identifications();
        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            returnValue.setState(hero, HeroState.Unidentified);
        }
        return returnValue;
    }
}