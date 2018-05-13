package org.hallock.dota.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hallock.dota.control.Config;
import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Identifications;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class NetworkManager {
    CloseableHttpClient httpclient;

    public NetworkManager(CloseableHttpClient aDefault) {
        this.httpclient = aDefault;
    }

    private String getPostUrl() {
        if (!Registry.getInstance().config.hasCreds()) {
            throw new IllegalStateException("No user credentials given!");
        }
        String url = Registry.getInstance().config.dotaPickerUrl;
        String steamId = Registry.getInstance().config.userId;
        String token = Registry.getInstance().config.userCred;
        return url + "/" + steamId + "/" + token;
    }

    public void sendPicks(Identifications.IdentificationResults results, RequestCallback callback) throws JSONException, IOException {
        if (callback == null) {
            callback = new DefaultCallback();
        }

        System.out.println("Posting content: " + results.toJson().toString(2));
        StringEntity postContent = new StringEntity(results.toJson().toString());
        String postUrl = getPostUrl();
        Registry.getInstance().logger.log("Posting to " + postUrl);

        HttpPost post = new HttpPost(postUrl);
        post.setEntity(postContent);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");


        int statusCode = -1;
        JSONObject responseContent = null;
        try (CloseableHttpResponse response = httpclient.execute(post);) {
            statusCode = response.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println("Response: " + responseString);
            responseContent = new JSONObject(responseString);
        }

        callback.received(statusCode, responseContent);
    }


    public void sendHeartbeat(RequestCallback callback) throws JSONException, IOException {

    }

    public interface RequestCallback {
        void received(int httpCode, JSONObject payload) throws JSONException;
    }

    private static class DefaultCallback implements RequestCallback {
        @Override
        public void received(int httpCode, JSONObject payload) throws JSONException {
            System.out.println("Response code: " + httpCode);
            System.out.println("Error: " + payload.getBoolean("error"));
            System.out.println("Energy: " + payload.getInt("energy"));
            System.out.println("Steam name: " + payload.getString("steam_name"));
            System.out.println("Steam avatar: " + payload.getString("steam_avatar"));
        }
    }

    public static NetworkManager buildNetworkManager() {
        return new NetworkManager(HttpClients.createDefault());
    }
}
