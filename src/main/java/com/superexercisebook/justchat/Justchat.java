package com.superexercisebook.justchat;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

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
        Text MSG = chatEvent.getRawMessage();
        //logger.info(MSG.toString());
        MessagePacker Pack = new MessagePacker(chatEvent,player);
        Pack.logger=logger;
        client.clientManager.send(Pack);
    }



}
