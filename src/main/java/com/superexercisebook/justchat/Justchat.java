package com.superexercisebook.justchat;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.AttackEntityEvent;
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

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("JustChat with your friends.");
        client = new JustchatClient();
        client.logger=logger;
        client.run();
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat chatEvent, @First Player player) {
        //Text MSG = chatEvent.getRawMessage();
        //logger.info(MSG.toString());
        MessagePacker_Chat Pack = new MessagePacker_Chat(chatEvent,player);
        client.clientManager.send(Pack);
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join loginEvent, @First Player player){
        MessagePacker_Info Pack = new MessagePacker_Info(MessagePackType.INFO_EventType_Join,player);
        client.clientManager.send(Pack);

    }


    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect disconnectEvent,  @First Player player){
        MessagePacker_Info Pack = new MessagePacker_Info(MessagePackType.INFO_EventType_Disconnect,player);
        client.clientManager.send(Pack);
    }



    @Listener
    public void onPlayerDamage(DamageEntityEvent event, @First IndirectEntityDamageSource source) {
        if ((event.getTargetEntity() instanceof Player) && (event.willCauseDeath())) {
            Player player = (Player) event.getTargetEntity();
            //Cause cause = event.getCause();
            MessagePacker_Info Pack = new MessagePacker_Info(MessagePackType.INFO_EventType_PlayerDead,player);
            client.clientManager.send(Pack);
        }
    }

/*
    @Listener
    public void onPlayerDamage(AttackEntityEvent event, @First EntityDamageSource source) {
        if ((event.getTargetEntity() instanceof Player)) {
            Player player = (Player) source.getSource();
        }
    }
*/

}
