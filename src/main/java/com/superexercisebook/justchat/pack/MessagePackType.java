package com.superexercisebook.justchat.pack;


public class MessagePackType {
    public static int PackVersion=3;

    public final static int PULSE = 0;
    public final static int REGISTRATION = 1;
    public final static int INFO = 100;
    public final static int MESSAGE = 101;

    public final static int PlayerList = 200;
    public final static int PlayerListRequest = 0;
    public final static int PlayerListResponse = 1;

    public final static int REGISTRATION_Identity = 0;

    public final static int INFO_EventType_Join = 1;
    public final static int INFO_EventType_Disconnect = 2;
    public final static int INFO_EventType_PlayerDead = 3;
}