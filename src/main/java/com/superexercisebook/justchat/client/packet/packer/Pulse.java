package com.superexercisebook.justchat.client.packet.packer;


import com.superexercisebook.justchat.client.packet.PacketType;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Pulse implements IPulseSendable {

    final static byte[] MessageHeader = {0x11, 0x45, 0x14};

    private String createAPulseData() {
        JSONObject jsonObject = new JSONObject();
        try {
            //Random rand = new Random();
            jsonObject.put("version", PacketType.PackVersion);
            jsonObject.put("type", PacketType.PULSE);
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
        String MSG = createAPulseData();
        byte[] body = MSG.getBytes(StandardCharsets.UTF_8);
        ByteBuffer bb = ByteBuffer.allocate(MessageHeader.length + 4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(MessageHeader);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }

}

