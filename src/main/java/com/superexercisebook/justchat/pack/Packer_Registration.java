package com.superexercisebook.justchat.pack;


import com.superexercisebook.justchat.Settings;
import org.json.JSONException;
import org.json.JSONObject;

public class Packer_Registration extends Packer  {
    //final static byte[] MessageHeader = {(byte)0x70,(byte)0x93,(byte)0x94};
    //int i=0;

    public Packer_Registration(Settings config){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", MessagePackType.PackVersion);
            jsonObject.put("type", MessagePackType.REGISTRATION);
            jsonObject.put("identity", MessagePackType.REGISTRATION_Identity);
            jsonObject.put("ID", MessageTools.Base64Encode(config.getGeneral().server().ID()) );
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

