package com.superexercisebook.justchat;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class MessagePacker implements ISendable {
    public static  int PackVersion=2;


    final static byte[] MessageHeader = {0x11,0x45,0x14};
    private String MSG = "";
    public Logger logger;

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



    public MessagePacker(MessageChannelEvent.Chat chatEvent, @First Player player){

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

    @Override
    public byte[] parse() {
        byte[] body = MSG.getBytes(Charset.forName("UTF-8"));
        ByteBuffer bb = ByteBuffer.allocate(MessageHeader.length+4+body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(MessageHeader);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }


    public static boolean isMessage(byte[] a){
        if ((a[0]==MessageHeader[0]) && (a[1]==MessageHeader[1]) && (a[2]==MessageHeader[2])) {
            return true;
        }
        else
        {
            return false;
        }
    }

}

