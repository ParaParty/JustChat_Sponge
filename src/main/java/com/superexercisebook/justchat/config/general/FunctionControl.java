package com.superexercisebook.justchat.config.general;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class FunctionControl {
    @Setting(comment = "Forward player dead original messages.")
    private boolean forwardPlayerDeadOriginalMessages = true;

    @Setting(comment = "Forward player logging and disconnecting messages.")
    private boolean forwardPlayerLoggingAndDisconnectionMessages = true;

    public boolean forwardPlayerDeadOriginalMessages(){
        return forwardPlayerDeadOriginalMessages;
    }

    public boolean forwardPlayerLoggingAndDisconnectionMessages(){
        return forwardPlayerLoggingAndDisconnectionMessages;
    }

}
