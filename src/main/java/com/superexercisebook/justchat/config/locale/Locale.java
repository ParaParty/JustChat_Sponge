package com.superexercisebook.justchat.config.locale;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Locale {
    @Setting(comment = "Message Display Format")
    private MessageFormat messageFormat = new MessageFormat();

    public MessageFormat messageFormat(){
        return messageFormat;
    }

}
