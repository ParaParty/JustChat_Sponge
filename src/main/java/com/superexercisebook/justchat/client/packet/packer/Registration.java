package com.superexercisebook.justchat.client.packet.packer;


import com.superexercisebook.justchat.config.Settings;
import com.superexercisebook.justchat.client.packet.MessageTools;
import com.superexercisebook.justchat.client.packet.PacketType;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends Packer  {

    public Registration(Settings config){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.REGISTRATION);
            jsonObject.put("identity", PacketType.REGISTRATION_Identity);
            jsonObject.put("id", MessageTools.Base64Encode(config.getGeneral().server().ID()) );
            jsonObject.put("name", MessageTools.Base64Encode(config.getGeneral().server().name()) );
            this.MSG = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static boolean isPulse(byte[] a){
        if ((a[0]==MessageHeader[0]) && (a[1]==MessageHeader[1]) && (a[2]==MessageHeader[2])) {
            return true;
        }
        else
        {
            return false;
        }
    }

}

