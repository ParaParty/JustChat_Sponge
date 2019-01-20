package com.superexercisebook.justchat;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;


public class MessagePacker_Chat extends MessagePacker{
    private JSONArray ContentPacker(String msg){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type","text");
            String content = msg.substring(5,msg.length()-1);
            jsonObject.put("content",content);

            jsonArray.put(jsonObject);
            return jsonArray;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }



    public MessagePacker_Chat(MessageChannelEvent.Chat chatEvent, @First Player player){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PackVersion);
            jsonObject.put("type", MessagePackType.MESSAGE);
            jsonObject.put("world", MessageTools.Base64Encode(player.getWorld().getName()));
            jsonObject.put("world_display", MessageTools.Base64Encode(player.getWorld().getName()));
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            jsonObject.put("content",ContentPacker(chatEvent.getRawMessage().toString()));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}

