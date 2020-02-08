package com.superexercisebook.justchat.client.packet.packer;

import com.superexercisebook.justchat.client.packet.MessageTools;
import com.superexercisebook.justchat.client.packet.PacketType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;


public class Chat extends Packer {
    private JSONArray ContentPacker(String msg){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type","text");
            jsonObject.put("content", MessageTools.Base64Encode(msg));
            jsonArray.put(jsonObject);
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public Chat(MessageChannelEvent.Chat chatEvent, @First Player player){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.MESSAGE);
            jsonObject.put("world", MessageTools.Base64Encode(player.getWorld().getName()));
            jsonObject.put("world_display", MessageTools.Base64Encode(player.getWorld().getName()));
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            jsonObject.put("content",ContentPacker(chatEvent.getRawMessage().toPlain()));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}

