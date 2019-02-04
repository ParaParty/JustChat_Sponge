package com.superexercisebook.justchat.pack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Iterator;


public class Packer_PlayerList extends Packer {
    int count=0;
    JSONArray content;


    void getPlayerList(){
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
            return;
        }
    }

    public Packer_PlayerList(){
        getPlayerList();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", MessagePackType.PackVersion);
            jsonObject.put("type", MessagePackType.PlayerList);
            jsonObject.put("subtype", MessagePackType.PlayerListResponse);
            jsonObject.put("max", Sponge.getServer().getMaxPlayers());
            jsonObject.put("count", count);
            jsonObject.put("playerlist",content);
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
