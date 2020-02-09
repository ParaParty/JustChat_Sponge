package com.superexercisebook.justchat.config.general;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;


@ConfigSerializable
public class General {

    @Setting(comment = "The server config")
    private Server server = new Server();

    @Setting(comment = "Function control")
    private FunctionControl functionControl = new FunctionControl();

    public Server server() {
        return server;
    }

    public FunctionControl functionControl() {
        return functionControl;
    }

}
