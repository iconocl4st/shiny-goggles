package org.hallock.dota.model.geometry;

import org.hallock.dota.control.Config;
import org.hallock.dota.model.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

public class GridEnumerator {

    public interface GridItemVisitor {
        void visit(JSONObject config, Rectangle location) throws JSONException;
    }

    public static void enumerateGrid(
            HeroGridGeometry grid,
            JSONObject gridConfig,
            GridItemVisitor visitor
    ) throws JSONException {
        int currentX = grid.heroStartX;
        int currentY = grid.heroStartY;
        JSONArray heroTypes = gridConfig.getJSONArray("types");
        for (int type=0; type<heroTypes.length(); type++) {
            JSONArray rows = heroTypes.getJSONArray(type);
            for (int r = 0; r < rows.length(); r++) {
                JSONArray heroes = rows.getJSONArray(r);
                for (int h = 0; h < heroes.length(); h++) {
                    JSONObject spec = heroes.getJSONObject(h);
                    Rectangle location = new Rectangle(currentX, currentY, grid.heroWidth, grid.heroHeight);
                    visitor.visit(spec, location);
                    currentX += grid.heroWidth + grid.heroHorizontalGap;
                }
                currentX = grid.heroStartX;
                currentY += grid.heroHeight + grid.heroVerticalGap;
            }
            currentY += grid.heroTypeGap - grid.heroVerticalGap;
        }
    }


    public interface RowItemVisitor {
        void visit(Rectangle location, Team team, int idx);
    }

    public static void enumerateGrid(
            ImageRowGeometry grid,
            RowItemVisitor visitor
    ) {
        int currentX = grid.startX;
        int currentY = grid.startY;
        for (Team team : new Team[]{ Team.RADIANT, Team.DIRE }) {
            for (int i=0; i<Config.TEAM_LENGTH; i++) {
                Rectangle location = new Rectangle(currentX, currentY, grid.width, grid.height);
                visitor.visit(location, team, i);
                currentX += grid.width + grid.horizontalGap;
            }
            currentX += grid.teamGap - grid.horizontalGap;
        }
    }
}
