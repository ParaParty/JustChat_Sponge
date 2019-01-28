package com.superexercisebook.justchat;

import com.google.inject.Inject;
import java.nio.file.Path;

import com.superexercisebook.justchat.pack.MessagePackType;
import com.superexercisebook.justchat.pack.Packer_Chat;
import com.superexercisebook.justchat.pack.Packer_Info;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "justchat",
        name = "JustChat",
        description = "test",
        url = "http://superexercisebook.com",
        authors = {
                "Eric_Lian"
        }//,
       //dependencies = {
        ///        @Dependency(id = "")
       // }
)
public class Justchat {

    JustchatClient client;
    com.superexercisebook.justchat.Settings config;


    @Inject
    Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfigDir;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("JustChat with your friends.");

        config = new com.superexercisebook.justchat.Settings(logger,defaultConfigDir);

        client = new JustchatClient();
        client.logger=logger;
        client.config=config;
        client.run();


    }

    public Logger getLogger(){
        return logger;
    }

    public com.superexercisebook.justchat.Settings getConfigManager(){
        return config;
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat chatEvent, @First Player player) {
        //Text MSG = chatEvent.getRawMessage();
        //logger.info(MSG.toString());
        Packer_Chat Pack = new Packer_Chat(chatEvent,player);
        client.clientManager.send(Pack);
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join loginEvent, @First Player player){
        Packer_Info Pack = new Packer_Info(MessagePackType.INFO_EventType_Join,player);
        client.clientManager.send(Pack);

    }


    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect disconnectEvent,  @First Player player){
        Packer_Info Pack = new Packer_Info(MessagePackType.INFO_EventType_Disconnect,player);
        client.clientManager.send(Pack);
    }


    @Listener
    public void onPlayerDamage(DamageEntityEvent event) {
        if ((event.getTargetEntity() instanceof Player) && (event.willCauseDeath())) {
            Player player = (Player) event.getTargetEntity();
            //Cause cause = event.getCause();
            Packer_Info Pack = new Packer_Info(MessagePackType.INFO_EventType_PlayerDead,player);
            client.clientManager.send(Pack);
        }
    }


}
