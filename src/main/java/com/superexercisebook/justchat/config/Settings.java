package com.superexercisebook.justchat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.superexercisebook.justchat.config.general.General;
import com.superexercisebook.justchat.config.locale.Locale;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;

public class Settings {
    private Logger logger;
    private Path dataFolder;


    private ObjectMapper<General>.BoundInstance general;
    private ObjectMapper<Locale>.BoundInstance textMapper;

    private final ConfigurationOptions options = getConfigurationOptions();

    Settings(Logger logger, @ConfigDir(sharedRoot = false) Path dataFolder)
    {
        this.logger=logger;
        this.dataFolder=dataFolder;

        try {
            logger.info(General.class.getDeclaredConstructor().toString());

            general = options.getObjectMapperFactory().getMapper(General.class).bindToNew();
            textMapper = options.getObjectMapperFactory().getMapper(Locale.class).bindToNew();
        } catch (NoSuchMethodException E) {
            logger.error("No such a Constructor", E);
        } catch (ObjectMappingException objMappingExc) {
            logger.error("Invalid plugin structure", objMappingExc);
        }

        load();
    }


    /*
        Copy from:
        https://github.com/games647/FlexibleLogin/blob/master/src/main/java/com/github/games647/flexiblelogin/config/Settings.java
    */
    private ConfigurationOptions getConfigurationOptions() {
        ConfigurationOptions defaults = ConfigurationOptions.defaults();

        TypeSerializerCollection serializers = defaults.getSerializers().newChild();

        //explicit set enum serializer because otherwise they will be interpreted as class with the requirement of
        //a public constructor

        return defaults.setSerializers(serializers);
    }

    private void load() {

        Path configFile = dataFolder.resolve("config.conf");
        Path textFile = dataFolder.resolve("locale.conf");

        try {
            if (Files.notExists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
            }

            if (Files.notExists(textFile)) {
                Files.createFile(textFile);
            }
        } catch (IOException ioEx) {
            logger.error("Failed to create default config file", ioEx);
        }

        loadMapper(general, configFile);
        loadMapper(textMapper, textFile);
    }


    private <T> void loadMapper(ObjectMapper<T>.BoundInstance mapper, Path file) {
        ConfigurationNode rootNode;
        if (mapper != null) {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(file).build();
            try {
                rootNode = loader.load(options.setShouldCopyDefaults(true));

                //load the config into the object
                mapper.populate(rootNode);

                //add missing default values
                loader.save(rootNode);
            } catch (ObjectMappingException objMappingExc) {
                logger.error("Error loading the configuration", objMappingExc);
            } catch (IOException ioExc) {
                logger.error("Error saving the default configuration", ioExc);
            }
        }
    }

    public General getGeneral() {
        return general.getInstance();
    }

    public Locale getText() {
        return textMapper.getInstance();
    }

}
