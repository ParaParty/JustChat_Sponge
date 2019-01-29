package com.superexercisebook.justchat.pack;


import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Random;

public class Packer_Pulse implements IPulseSendable {
    //final static byte[] MessageHeader = {(byte)0x70,(byte)0x93,(byte)0x94};
    //int i=0;



    final static byte[] MessageHeader = {0x11,0x45,0x14};

    private String createAPulseData(){
        JSONObject jsonObject = new JSONObject();
        try {
            //Random rand = new Random();
            jsonObject.put("version", MessagePackType.PackVersion);
            jsonObject.put("type", MessagePackType.PULSE);
            //jsonObject.put("id", rand.nextInt(2147483647));
            String MSG = jsonObject.toString();
            return MSG;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public byte[] parse() {
        String MSG=createAPulseData();
        byte[] body = MSG.getBytes(Charset.forName("UTF-8"));
        ByteBuffer bb = ByteBuffer.allocate(MessageHeader.length+4+body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(MessageHeader);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
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

