package com.superexercisebook.justchat.client;

import com.superexercisebook.justchat.GlobalState;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

public class Client extends Thread {

    public IConnectionManager clientManager;

    public Client() {
    }

    @Override
    public void run() {
        ConnectionInfo info = new ConnectionInfo(GlobalState.config.getGeneral().server().ip(), GlobalState.config.getGeneral().server().port());
        GlobalState.logger.info("Target server: " + GlobalState.config.getGeneral().server().ip() + ":" + GlobalState.config.getGeneral().server().port());
        clientManager = OkSocket.open(info);

        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setReaderProtocol(new ProtocolDefinition());
        clientManager.option(okOptionsBuilder.build());

        clientManager.getReconnectionManager().addIgnoreException(ReloadException.class);

        MessageHandler justChatClientHandler = new MessageHandler(clientManager, okOptionsBuilder);
        clientManager.registerReceiver(justChatClientHandler);

        clientManager.connect();
    }

    public void updateConfig() {
        ConnectionInfo info = new ConnectionInfo(GlobalState.config.getGeneral().server().ip(), GlobalState.config.getGeneral().server().port());
        clientManager.disconnect(new ReloadException(info));
    }



}
