package com.superexercisebook.justchat.pack;

import org.json.JSONException;
import org.json.JSONObject;

import org.spongepowered.api.entity.living.player.Player;

public class Packer_Info extends Packer {

    public Packer_Info(int eventType, Player player){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", MessagePackType.PackVersion);
            jsonObject.put("type", MessagePackType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Packer_Info(int eventType, Player player, String content){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", MessagePackType.PackVersion);
            jsonObject.put("type", MessagePackType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("sender", MessageTools.Base64Encode(player.getName()));
            jsonObject.put("content", MessageTools.Base64Encode(content));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Packer_Info(int eventType, String content){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", MessagePackType.PackVersion);
            jsonObject.put("type", MessagePackType.INFO);
            jsonObject.put("event", eventType);
            jsonObject.put("content", MessageTools.Base64Encode(content));
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

