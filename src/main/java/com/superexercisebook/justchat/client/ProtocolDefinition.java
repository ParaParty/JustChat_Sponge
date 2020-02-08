package com.superexercisebook.justchat.client;

import com.superexercisebook.justchat.client.packet.packer.Packer;
import com.xuhao.didi.core.protocol.IReaderProtocol;

import java.nio.ByteOrder;

public class ProtocolDefinition implements IReaderProtocol {
    /**
     * Header of a packet.
     */
    private final static byte[] MessageHeader = {0x11, 0x45, 0x14};

    /**
     * Get header length of a packet.
     *
     * @return The length of a packet header.
     */
    @Override
    public int getHeaderLength() {
        return MessageHeader.length + 4;
    }

    /**
     * Get body length of a packet.
     *
     * @param header    The header of a packet according to the return of getHeaderLength()
     * @param byteOrder Byte order
     * @return The length of a packet body.
     */
    @Override
    public int getBodyLength(byte[] header, ByteOrder byteOrder) {
        if (Packer.isMessage(header)) {
            return (header[3] & 0xff) * (2 << 23) +
                    (header[4] & 0xff) * (2 << 15) +
                    (header[5] & 0xff) * (2 << 7) +
                    (header[6] & 0xff);
        }
        ;
        return 0;
    }
}
