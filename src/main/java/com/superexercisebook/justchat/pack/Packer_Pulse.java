package com.superexercisebook.justchat.pack;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Packer_Pulse extends Packer  {
    //final static byte[] MessageHeader = {(byte)0x70,(byte)0x93,(byte)0x94};
    //int i=0;

    public Packer_Pulse(){
        JSONObject jsonObject = new JSONObject();
        try {
            Random rand = new Random();
            jsonObject.put("version", PackVersion);
            jsonObject.put("type", MessagePackType.PULSE);
            jsonObject.put("id", rand.nextInt(2147483647));
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

