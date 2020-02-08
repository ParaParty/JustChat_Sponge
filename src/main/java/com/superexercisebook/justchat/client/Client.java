package com.superexercisebook.justchat.client;

import com.superexercisebook.justchat.config.Settings;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import org.slf4j.Logger;

public class Client extends Thread {

    private ConnectionInfo info;
    public IConnectionManager clientManager;

    private Logger logger;
    private Settings config;

    /**
     * Constructor
     *
     * @param logger Game logger.
     * @param config Plugin configuration
     */
    public Client(Logger logger, Settings config) {
        this.logger = logger;
        this.config = config;
    }

    @Override
    public void run() {

        info = new ConnectionInfo(config.getGeneral().server().ip(), config.getGeneral().server().port());
        logger.info("Target server: " + config.getGeneral().server().ip() + ":" + config.getGeneral().server().port());

        // initialize
        clientManager = OkSocket.open(info);

        // setting
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setReaderProtocol(new ProtocolDefinition());
        clientManager.option(okOptionsBuilder.build());

        MessageHandler justChatClientHandler = new MessageHandler(logger, config, clientManager, okOptionsBuilder);
        clientManager.registerReceiver(justChatClientHandler);

        // Confirm
        clientManager.connect();
    }
}
