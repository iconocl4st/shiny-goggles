package org.hallock.dota.model.geometry;

import org.json.JSONException;
import org.json.JSONObject;

public class HeroGridGeometry {
    private static final String DX = "horizontal-gap";
    private static final String DY = "vertical-gap";
    private static final String DTY = "type-vertical-gap";
    private static final String HERO_WIDTH = "hero-width";
    private static final String HERO_HEIGHT = "hero-height";
    private static final String HERO_START_X = "start-x";
    private static final String HERO_START_Y = "start-y";


    public int heroHorizontalGap = 7;
    public int heroVerticalGap = 6;
    public int heroWidth = 45;
    public int heroHeight = 73;
    public int heroTypeGap = 32;

    public int heroStartX = 172;
    public int heroStartY = 188;

    HeroGridGeometry(JSONObject obj) throws JSONException {
        heroHorizontalGap = obj.getInt(DX);
        heroVerticalGap = obj.getInt(DY);
        heroWidth = obj.getInt(HERO_WIDTH);
        heroHeight = obj.getInt(HERO_HEIGHT);
        heroTypeGap = obj.getInt(DTY);

        heroStartX = obj.getInt(HERO_START_X);
        heroStartY = obj.getInt(HERO_START_Y);
    }
    public HeroGridGeometry() {}

    public static HeroGridGeometry parseHeroGridGeometry(JSONObject object) {
        return new HeroGridGeometry();
    }
}
