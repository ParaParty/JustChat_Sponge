package com.superexercisebook.justchat;

import com.google.inject.Inject;

import java.nio.file.Path;

import com.superexercisebook.justchat.config.Settings;
import com.superexercisebook.justchat.client.Client;
import com.superexercisebook.justchat.client.packet.PacketType;
import com.superexercisebook.justchat.client.packet.packer.Chat;
import com.superexercisebook.justchat.client.packet.packer.Info;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "justchat",
        name = "JustChat",
        description = "JustChat with your friends",
        url = "http://superexercisebook.com",
        authors = {
                "Eric_Lian"
        }
)
public class Justchat {


    private Client client;

    private Settings config;

    @Inject
    Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfigDir;

    /**
     * Initialization
     *
     * @param event Event object from Sponge
     */
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("JustChat with your friends.");

        config = new Settings(logger, defaultConfigDir);

        client = new Client(logger, config);
        client.start();


    }

    public Logger getLogger() {
        return logger;
    }

    public Settings getConfigManager() {
        return config;
    }

    /**
     * Fetch chatting message from game.
     *
     * @param chatEvent Event object from Sponge.
     * @param player    The sender of the message.
     */
    @Listener(order = Order.POST)
    public void onChat(MessageChannelEvent.Chat chatEvent, @First Player player) {
        Chat Pack = new Chat(chatEvent, player);
        client.clientManager.send(Pack);
    }

    /**
     * Fetch logging message from game.
     *
     * @param loginEvent Event object from Sponge.
     * @param player     The sender of the message.
     */
    @Listener(order = Order.DEFAULT)
    public void onPlayerLogin(ClientConnectionEvent.Join loginEvent, @First Player player) {
        Info Pack = new Info(PacketType.INFO_EventType_Join, player);
        client.clientManager.send(Pack);

    }

    /**
     * Fetch disconnecting message from game.
     *
     * @param disconnectEvent Event object from Sponge.
     * @param player          The sender of the message.
     */
    @Listener(order = Order.DEFAULT)
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect disconnectEvent, @First Player player) {
        Info Pack = new Info(PacketType.INFO_EventType_Disconnect, player);
        client.clientManager.send(Pack);
    }

    /**
     * Fetch died message from game.
     *
     * @param event Event object from Sponge.
     */
    @Listener(order = Order.POST)
    public void onPlayerDead(DestructEntityEvent.Death event) {
        if ((!event.isCancelled()) && (event.getTargetEntity() instanceof Player)) {
            Player player = (Player) event.getTargetEntity();
            Info Pack = new Info(PacketType.INFO_EventType_PlayerDead, player, event.getOriginalMessage().toPlain());
            client.clientManager.send(Pack);
        }
    }
}
