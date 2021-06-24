package com.geora.socket;

import android.view.View;

import org.java_websocket.handshake.ServerHandshake;

public interface SocketCallback {
    void onOpen(ServerHandshake serverHandshake);

    void onMessage(String msg);

    void onClose(int code, String reason, boolean remote);

    void onError(Exception ex);


}
