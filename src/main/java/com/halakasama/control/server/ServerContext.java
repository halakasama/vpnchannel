package com.halakasama.control.server;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import org.apache.commons.codec.binary.StringUtils;

import java.util.Map;

/**
 * Created by admin on 2017/3/28.
 */
public class ServerContext implements LocalContextHelper{
    private String serverUid;
    private QRNG qrng;
    private Map<>

    public ServerContext(){
        serverUid = GlobalParam.SERVER_UID;
        qrng = QRNG.getInstance();
    }

    public static void main(String[] args) {
        byte[] bytes = StringUtils.getBytesUtf8("abcde大");
        String string = StringUtils.newStringUtf8(bytes);
        System.out.println(string);
    }

    @Override
    public String getLocalUid() {
        return serverUid;
    }

    @Override
    public boolean checkUidValid(String uidBytes) {
        return true;
    }

    @Override
    public byte[] getRandomBytes(int size) {
        return qrng.getRandomBytes(size);
    }

    @Override
    public byte[] getCurrentKey(String uid, int keyPtr, int size) {
        return new byte[0];
    }

    @Override
    public void registerConnection(ConnectContext connectContext) {

    }

    @Override
    public void unregisterConnection(ConnectContext connectContext) {

    }
}
