package com.superexercisebook.justchat.client.packet.packer;

import com.superexercisebook.justchat.client.packet.MessageTools;
import com.superexercisebook.justchat.client.packet.PacketType;
import org.json.JSONException;
import org.json.JSONObject;

import org.spongepowered.api.entity.living.player.Player;

public class Info extends Packer {

    public Info(int eventType, Player player) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Info(int eventType, Player player, String content) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            jsonObject.put("content", MessageTools.Base64Encode(content));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Info(int eventType, String content) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("content", MessageTools.Base64Encode(content));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

