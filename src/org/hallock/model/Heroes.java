package org.hallock.model;

import org.hallock.control.ApplicationContext;
import org.hallock.util.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;
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

    public static Heroes buildHeroes(JSONObject heroConfig) {
        JSONArray array = heroConfig.getJSONArray("heroes");
        HashMap<String, Hero> heroes = new HashMap<>();
        for (int i=0; i<array.length(); i++) {
            JSONObject hero = array.getJSONObject(i);
            String id = hero.getString("id");
            Integer pickerId = hero.getInt("pickerId");
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