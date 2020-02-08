package com.superexercisebook.justchat.client.packet.packer;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public abstract class Packer implements ISendable {
    String MSG = "";

    final static byte[] MessageHeader = {0x11, 0x45, 0x14};

    @Override
    public byte[] parse() {
        byte[] body = MSG.getBytes(StandardCharsets.UTF_8);
        ByteBuffer bb = ByteBuffer.allocate(MessageHeader.length + 4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(MessageHeader);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }

    public static boolean isMessage(byte[] a) {
        return (a[0] == MessageHeader[0]) && (a[1] == MessageHeader[1]) && (a[2] == MessageHeader[2]);
    }
}
