package com.halakasama.packet;

import org.msgpack.annotation.Message;

import java.util.Arrays;

/**
 * Created by pengfei.ren on 2017/3/14.
 */

@Message
public class DataPacket {
    public String srcAddress;
    public String dstAddress;
    public byte[] data;

    public DataPacket(){};
    public DataPacket(String srcAddress, String dstAddress, byte[] data) {
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataPacket{" +
                "srcAddress='" + srcAddress + '\'' +
                ", dstAddress='" + dstAddress + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
