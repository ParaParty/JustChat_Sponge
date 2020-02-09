package com.superexercisebook.justchat.config.general;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class FunctionControl {
    @Setting(comment = "Forward players dead original messages.")
    private boolean forwardPlayersDeadOriginalMessages = true;

    @Setting(comment = "Forward players logging and disconnecting messages.")
    private boolean forwardPlayersLoggingAndDisconnectionMessages = true;

    public boolean forwardPlayersDeadOriginalMessages() {
        return forwardPlayersDeadOriginalMessages;
    }

    public boolean forwardPlayersLoggingAndDisconnectionMessages() {
        return forwardPlayersLoggingAndDisconnectionMessages;
    }

}
