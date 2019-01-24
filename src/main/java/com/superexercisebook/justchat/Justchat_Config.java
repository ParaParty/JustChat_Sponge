package com.superexercisebook.justchat;

import com.google.common.reflect.TypeToken;
import com.sun.jndi.cosnaming.ExceptionMapper;
import com.typesafe.config.ConfigException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import static org.spongepowered.api.text.TextTemplate.*;

public class Justchat_Config {
    private Logger logger;
    private Path dataFolder;


    Justchat_Config(Logger logger, @ConfigDir(sharedRoot = false) Path dataFolder)
    {
        this.logger=logger;
        this.dataFolder=dataFolder;

        load();
    }


    /*
        Copy from:
        https://github.com/games647/FlexibleLogin/blob/master/src/main/java/com/github/games647/flexiblelogin/config/Settings.java
    */
    void load(){

        Path configFile = dataFolder.resolve("config.conf");
        Path textFile = dataFolder.resolve("locale.conf");

        try {
            if (Files.notExists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
                CreateDefaultConfigFile(configFile);
            }

            if (Files.notExists(textFile)) {
                Files.createFile(textFile);
                CreateDefaultLocaleFile(textFile);
            }
        } catch (IOException ioEx) {
            logger.error("Failed to create default config file", ioEx);
        }

        LoadConfigFile(configFile);

    }

    void CreateDefaultConfigFile(Path configFile){
        ConfigurationNode settings;
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configFile).build();
        try {
            settings = loader.load();
            settings.getNode("server").getNode("ip").setValue("127.0.0.1");
            settings.getNode("server").getNode("port").setValue(38440);
            loader.save(settings);
        } catch (ConfigException ConfigExc){
            logger.error("Error creating the default configuration", ConfigExc);
        } catch (IOException ioExc) {
            logger.error("Error saving the default configuration", ioExc);
        }
    }

    void LoadConfigFile(Path configFile){
        ConfigurationNode settings;
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configFile).build();
        try {
            settings = loader.load();
            Settings.ip=settings.getNode("server").getNode("ip").getString("127.0.0.1");
            Settings.port=settings.getNode("server").getNode("port").getInt(38440);
            loader.save(settings);
        } catch (ConfigException ConfigExc){
            logger.error("Error loading the config file", ConfigExc);
        } catch (IOException ioExc) {
            logger.error("Error saving the default configuration", ioExc);
        }
    }


    void CreateDefaultLocaleFile(Path textFile){
        TextTemplate MSGFormat_overview = of(
                TextColors.DARK_GREEN,"[*]",
                arg("SENDER").color(TextColors.DARK_GREEN),
                TextColors.RESET,": ",
                arg("BODY")
        );

        TextTemplate MSGFormat_text = of(
                arg("CONTENT")
        );

        TextTemplate MSGFormat_at = of(
                arg("TARGET").color(TextColors.BLUE)
        );

        TextTemplate MSGFormat_image = of(
                arg("CONTENT").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
        );

        ConfigurationNode settings;
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(textFile).build();
        try {
            settings = loader.load();
            settings.getNode("MSGFormat").getNode("overview").setValue(TypeToken.of(TextTemplate.class),MSGFormat_overview);
            settings.getNode("MSGFormat").getNode("text").setValue(TypeToken.of(TextTemplate.class),MSGFormat_text);
            settings.getNode("MSGFormat").getNode("at").setValue(TypeToken.of(TextTemplate.class),MSGFormat_at);
            settings.getNode("MSGFormat").getNode("image").setValue(TypeToken.of(TextTemplate.class),MSGFormat_image);
            loader.save(settings);
        } catch (ObjectMappingException MapExc){
            logger.error("Error creating/saving the default configuration", MapExc);
        } catch (ConfigException ConfigExc){
            logger.error("Error creating the default configuration", ConfigExc);
        } catch (IOException ioExc) {
            logger.error("Error saving the default configuration", ioExc);
        }
    }

    public static class Settings{
        static String ip="127.0.0.1";
        static int port=38440;
    }

    public static class Locale{

    }

}
