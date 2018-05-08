package org.hallock.util;

import org.hallock.control.Config;
import org.json.JSONObject;

import java.net.Socket;

public class NetworkManager {
    Socket socket;

    public NetworkManager(Socket socket, Config config) {

    }

    public void sendStatus(RequestCallback callback) {

    }

    public void sendHeartbeat(RequestCallback callback) {

    }

    public static interface RequestCallback {
        public void response(JSONObject payload);
    }
}
