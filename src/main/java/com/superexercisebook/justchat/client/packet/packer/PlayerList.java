package com.superexercisebook.justchat.client.packet.packer;

import com.superexercisebook.justchat.client.packet.MessageTools;
import com.superexercisebook.justchat.client.packet.PacketType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Iterator;


public class PlayerList extends Packer {
    private int count = 0;
    private JSONArray content;


    private void getPlayerList() {
        try {
            Iterator<Player> playerList = Sponge.getServer().getOnlinePlayers().iterator();
            //List<String> list = new ArrayList();

            content = new JSONArray();
            while (playerList.hasNext()) {
                Player aPlayer = playerList.next();
                count++;
                content.put(MessageTools.Base64Encode(aPlayer.getName()));
                //list.add(aPlayer.getName());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public PlayerList(String sender, String world, String world_display) {
        getPlayerList();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.PlayerList);
            jsonObject.put("subtype", PacketType.PlayerListResponse);
            jsonObject.put("max", Sponge.getServer().getMaxPlayers());
            jsonObject.put("count", count);
            jsonObject.put("player_list", content);
            jsonObject.put("sender", sender);
            jsonObject.put("world", world);
            jsonObject.put("world_display", world_display);
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
