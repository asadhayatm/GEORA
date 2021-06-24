package com.geora.socket;

import android.util.Log;

import com.geora.data.DataManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class SocketManager {
    public static WebSocketClient mWebSocketClient;
    public static SocketManager instance;
    public static SocketCallback callback;
    public static String request = "";
    private static boolean inBackground = false;

    public SocketManager() {
    }

    public void setSocketCallbackListener(SocketCallback callback) {
        this.callback = callback;
    }

    public static void connectWebSocket() {
        URI uri;
        try {
            uri = new URI(SocketConstant.URL.SERVER_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("authorization", DataManager.getInstance().getAccessToken());

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                if (callback != null)
                    callback.onOpen(handshakedata);
                Log.d("MyApp", "\n\n----------------------------------Socket Open------------------------------------\n");
                if (!request.equalsIgnoreCase(""))
                    mWebSocketClient.send(request);
            }

            @Override
            public void onMessage(String message) {
                if (callback != null)
                    callback.onMessage(message);
                Log.d("MyApp", "\n\n----------------------------------Socket message------------------------------------ \n" + message + "\n\n");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (callback != null)
                    callback.onClose(code, reason, remote);
                Log.d("MyApp", "\n\n----------------------------------Socket Closed------------------------------------\n");
                if (!inBackground) connectWebSocket();

            }

            @Override
            public void onError(Exception ex) {
                if (callback != null)
                    callback.onError(ex);
                Log.d("MyApp", "\n\n----------------------------------Socket Exceprion------------------------------------\n" + ex + "\n\n");
            }
        };
        try {
            mWebSocketClient.connect();
            Log.d("MyApp", "\n\n----------------------------------Socket Connected------------------------------------\n");

        } catch (Exception e) {

        }
    }

    public void sendData(String request) {
        if (mWebSocketClient.getConnection().isOpen()) {
            mWebSocketClient.send(request);
            Log.d("MyApp", "\n\n----------------------------------Socket Request------------------------------------\n" + request + "\n\n");
        } else {
            this.request = request;
            connectWebSocket();
        }
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call init() before getInstance()");
        }
        return instance;
    }

    public synchronized static SocketManager init() {
        if (instance == null) {
            instance = new SocketManager();
            inBackground = false;
        }
        connectWebSocket();
        return instance;
    }

    public synchronized static void disconnectSocket() {
        if (mWebSocketClient != null) {
            try {
                inBackground = true;
                mWebSocketClient.close();
                Log.d("MyApp", "\n\n----------------------------Socket Disconnected-----------------------------------\n");
            } catch (Exception e) {

            }
        }
    }
}
