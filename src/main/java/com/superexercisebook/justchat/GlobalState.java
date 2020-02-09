package com.superexercisebook.justchat;

import com.superexercisebook.justchat.client.Client;
import com.superexercisebook.justchat.config.Settings;
import org.slf4j.Logger;

import java.nio.file.Path;

public class GlobalState {
    public static Settings config;
    public static Logger logger;
    public static Path dataFolder;
    public static Client client;
}
