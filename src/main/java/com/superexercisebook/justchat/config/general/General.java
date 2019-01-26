package com.superexercisebook.justchat.config.general;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;


@ConfigSerializable
public class General {

    @Setting(comment = "The server config")
    private Server server=new Server();

    public Server server(){
        return server;
    }



}
