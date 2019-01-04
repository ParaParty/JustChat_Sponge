package com.superexercisebook.justchat;


import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PulsePacker implements IPulseSendable  {
    final static byte[] MessageHeader = {(byte)0x70,(byte)0x93,(byte)0x94};
    //int i=0;

    @Override
    public byte[] parse() {
        //i++;
        ByteBuffer bb = ByteBuffer.allocate(MessageHeader.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(MessageHeader);
        //bb.putInt(i);
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

