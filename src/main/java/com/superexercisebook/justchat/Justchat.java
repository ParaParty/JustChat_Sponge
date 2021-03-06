package com.superexercisebook.justchat;

import com.google.inject.Inject;

import java.nio.file.Path;

import com.superexercisebook.justchat.command.CommandRegister;
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

        GlobalState.logger = logger;
        GlobalState.dataFolder = defaultConfigDir;

        CommandRegister commandRegister = new CommandRegister(this);

        GlobalState.config = new Settings();
        GlobalState.client = new Client();
        GlobalState.client.start();
    }

    /**
     * Fetch chatting message from game.
     *
     * @param chatEvent Event object from Sponge.
     * @param player    The sender of the message.
     */
    @Listener(order = Order.POST)
    public void onChat(MessageChannelEvent.Chat chatEvent, @First Player player) {
        if (!player.hasPermission("justchat.forward.chat")) return;

        Chat pack = new Chat(chatEvent, player);
        GlobalState.client.clientManager.send(pack);
    }

    /**
     * Fetch logging message from game.
     *
     * @param loginEvent Event object from Sponge.
     * @param player     The sender of the message.
     */
    @Listener(order = Order.DEFAULT)
    public void onPlayerLogin(ClientConnectionEvent.Join loginEvent, @First Player player) {
        if (!player.hasPermission("justchat.forward.network.join")) return;

        Info pack = null;
        if (GlobalState.config.getGeneral().functionControl().forwardPlayersLoggingAndDisconnectionMessages()) {
            pack = new Info(PacketType.INFO_EventType_Join, player, loginEvent.getMessage().toPlain());
        } else {
            pack = new Info(PacketType.INFO_EventType_Join, player);
        }
        GlobalState.client.clientManager.send(pack);
    }

    /**
     * Fetch disconnecting message from game.
     *
     * @param disconnectEvent Event object from Sponge.
     * @param player          The sender of the message.
     */
    @Listener(order = Order.DEFAULT)
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect disconnectEvent, @First Player player) {
        if (!player.hasPermission("justchat.forward.network.disconnect")) return;

        Info pack = new Info(PacketType.INFO_EventType_Disconnect, player);
        GlobalState.client.clientManager.send(pack);
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

            if (!player.hasPermission("justchat.forward.death")) return;

            Info pack = null;
            if (GlobalState.config.getGeneral().functionControl().forwardPlayersDeadOriginalMessages()) {
                pack = new Info(PacketType.INFO_EventType_PlayerDead, player, event.getMessage().toPlain());
            } else {
                pack = new Info(PacketType.INFO_EventType_PlayerDead, player);
            }
            GlobalState.client.clientManager.send(pack);
        }
    }
}
