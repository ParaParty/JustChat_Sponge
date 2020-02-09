package com.superexercisebook.justchat.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.superexercisebook.justchat.GlobalState;
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
    private ObjectMapper<General>.BoundInstance general;
    private ObjectMapper<Locale>.BoundInstance textMapper;

    private final ConfigurationOptions options = getConfigurationOptions();

    public Settings() {
        try {
            general = options.getObjectMapperFactory().getMapper(General.class).bindToNew();
            textMapper = options.getObjectMapperFactory().getMapper(Locale.class).bindToNew();
        } catch (ObjectMappingException objMappingExc) {
            GlobalState.logger.error("Invalid plugin structure", objMappingExc);
        }
        load();
    }


    /**
     * Reference https://github.com/games647/FlexibleLogin/blob/master/src/main/java/com/github/games647/flexiblelogin/config/Settings.java
     *
     * @return
     */
    private ConfigurationOptions getConfigurationOptions() {
        ConfigurationOptions defaults = ConfigurationOptions.defaults();

        TypeSerializerCollection serializers = defaults.getSerializers().newChild();

        //explicit set enum serializer because otherwise they will be interpreted as class with the requirement of
        //a public constructor

        return defaults.setSerializers(serializers);
    }

    private void load() {

        Path configFile = GlobalState.dataFolder.resolve("config.conf");
        Path textFile = GlobalState.dataFolder.resolve("locale.conf");

        try {
            if (Files.notExists(GlobalState.dataFolder)) {
                Files.createDirectories(GlobalState.dataFolder);
            }

            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
            }

            if (Files.notExists(textFile)) {
                Files.createFile(textFile);
            }
        } catch (IOException ioEx) {
            GlobalState.logger.error("Failed to create default config file", ioEx);
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
                GlobalState.logger.error("Error loading the configuration", objMappingExc);
            } catch (IOException ioExc) {
                GlobalState.logger.error("Error saving the default configuration", ioExc);
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
