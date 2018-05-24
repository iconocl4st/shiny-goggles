package org.hallock.dota.control.others;

import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Identifications;
import org.json.JSONException;

import java.io.IOException;

public class TestPost {
    public static void testPost() throws IOException, JSONException {
            Identifications.IdentificationResults results = new Identifications.IdentificationResults();
            results.banned.add(119);
            results.radiantPicked.add(120);
            results.direPicked.add(121);
            results.playerIsRadiant = true;
            Registry.getInstance().networkManager.sendPicks(results, null);
    }
}
