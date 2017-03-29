package com.halakasama.control.server;

import org.apache.commons.codec.binary.StringUtils;

import java.util.Arrays;

/**
 * Created by admin on 2017/3/28.
 */
public class ServerContext {
    String serverUid;
    byte[] serverUidBytes;
    QRNG qrng;
    public ServerContext(){
        serverUid = "0";
        serverUidBytes = StringUtils.getBytesUtf8(serverUid);
        qrng = QRNG.getInstance();
    }


    public String getServerUid() {
        return serverUid;
    }

    public byte[] getServerUidBytes() {
        return serverUidBytes;
    }

    public static void main(String[] args) {
        byte[] bytes = StringUtils.getBytesUtf8("abcde大");
        String string = StringUtils.newStringUtf8(bytes);
        System.out.println(string);
    }
}
