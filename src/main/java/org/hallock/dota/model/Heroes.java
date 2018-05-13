package org.hallock.dota.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;

public class Heroes {
    HashMap<String, Hero> heroes;

    Heroes(HashMap<String, Hero> heroes) {
        this.heroes = heroes;
    }

    public Hero getHero(String id) {
        return heroes.get(id);
    }

    public static Heroes buildHeroes(JSONObject heroConfig) throws JSONException {
        JSONArray array = heroConfig.getJSONArray("heroes");
        HashMap<String, Hero> heroes = new HashMap<>();
        for (int i=0; i<array.length(); i++) {
            JSONObject hero = array.getJSONObject(i);
            String id = hero.getString("id");
            Integer pickerId = hero.getInt("pickId");
            String display = hero.getString("display");
            heroes.put(id, new Hero(id, pickerId, display));
        }
        return new Heroes(heroes);
    }

    public Collection<Hero> getAll() {
        return heroes.values();
    }
}

























/*

abbaadon, axe, beastmaster, brewmaster, bristleback, centour, chaos knight, clockwork,
 */