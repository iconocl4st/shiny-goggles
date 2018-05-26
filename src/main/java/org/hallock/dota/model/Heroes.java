package org.hallock.dota.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Heroes {
    HashMap<String, Hero> heroes;
    HashMap<Integer, Hero> heroesByPickId;

    Heroes(HashMap<String, Hero> heroes) {
        this.heroes = heroes;

        heroesByPickId = new HashMap<>();
        for (Hero hero : heroes.values()) {
            heroesByPickId.put(hero.pickId, hero);
        }
    }

    public Hero getHero(String id) {
        Hero returnValue = heroes.get(id);
        if (returnValue == null) {
            throw new IllegalArgumentException("Unknown hero: " + id);
        }
        return returnValue;
    }

    public int getMaximumIndex() {
        int maximum = Integer.MIN_VALUE;
        for (Hero hero : heroes.values()) {
            int index = hero.getIndex();
            if (index > maximum) {
                maximum = index;
            }
        }
        return maximum;
    }

    public Hero getHeroByPickId(Integer pickId) {
        Hero returnValue = heroesByPickId.get(pickId);
        if (returnValue == null) {
            throw new IllegalArgumentException("Unknown pick id: " + pickId);
        }
        return returnValue;
    }

    public static Heroes buildHeroes(JSONObject heroConfig) throws JSONException {
        JSONArray array = heroConfig.getJSONArray("heroes");
        HashMap<String, Hero> heroes = new HashMap<>();
        for (int i=0; i<array.length(); i++) {
            Hero hero = new Hero(array.getJSONObject(i));
            heroes.put(hero.id, hero);
            hero.updateCache();
        }
        return new Heroes(heroes);
    }

    public Hero[] getAll() {
        return heroes.values().toArray(new Hero[0]);
    }

    public Hero getHeroByIndex(int heroIndex) {
        return getHeroByPickId(heroIndex);
    }
}

























/*

abbaadon, axe, beastmaster, brewmaster, bristleback, centour, chaos knight, clockwork,
 */