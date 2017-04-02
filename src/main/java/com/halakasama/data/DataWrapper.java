package com.halakasama.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class DataWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataWrapper.class);
    private static final int HEADER_LEN = 4 + 4;
    String dstAddress;
    int keyPtr;
    byte[] data;
    byte[] hmac;

    public static byte[] encode(DataWrapper dataWrapper){
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(dataWrapper.dstAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Invalid ip address {}.", dataWrapper.dstAddress);
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_LEN + dataWrapper.data.length + dataWrapper.hmac.length);
        byteBuffer.clear();
        byteBuffer.put(inetAddress.getAddress());
        byteBuffer.putInt(dataWrapper.keyPtr);
        byteBuffer.put(dataWrapper.data);
        byteBuffer.put(dataWrapper.hmac);
        return byteBuffer.array();
    }

    public static DataWrapper decode(byte[] message, int length){
        DataWrapper dataWrapper = new DataWrapper();
        return dataWrapper;
    }
}
