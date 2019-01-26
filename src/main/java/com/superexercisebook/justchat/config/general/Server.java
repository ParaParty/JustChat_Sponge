package com.superexercisebook.justchat.config.general;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.UUID;

@ConfigSerializable
public class Server {

    @Setting(comment = "The IP address of JustChat server")
    private String ip="127.0.0.1";

    @Setting(comment = "The Port number of JustChat server")
    private int port=54321;

    @Setting(comment = "The ID of this console")
    private String ID=UUID.randomUUID().toString();

    public String ip(){ return ip; }

    public int port(){
        return port;
    }

    public String ID(){ return ID; }

}
