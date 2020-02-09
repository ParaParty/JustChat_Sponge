package com.superexercisebook.justchat.client;

import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;

class ReloadException extends Exception {


    private final ConnectionInfo newConnection;

    ReloadException(ConnectionInfo newConnection) {
        this.newConnection = newConnection;
    }

    ConnectionInfo getNewConnection(){
        return newConnection;
    }

}
