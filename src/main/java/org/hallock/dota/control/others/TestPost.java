package org.hallock.dota.control.others;

import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Identifications;
import org.json.JSONException;

import java.io.IOException;

public class TestPost {
    public static void testPost() throws IOException, JSONException {
            System.exit(0);

            System.out.println(Registry.getInstance().picker.identifyPicks().toJson().toString(2));

            Identifications.IdentificationResults results = new Identifications.IdentificationResults();
            results.banned.add(1);
            results.banned.add(2);
            results.banned.add(3);
            results.banned.add(4);
            results.banned.add(5);
            results.banned.add(6);
            results.radiantPicked.add(1);
            results.radiantPicked.add(8);
            results.radiantPicked.add(9);
            results.direPicked.add(10);
            results.direPicked.add(11);
            results.unavailable.add(12);
            results.playerIsRadiant = true;
            Registry.getInstance().networkManager.sendPicks(results, null);
    }
}
