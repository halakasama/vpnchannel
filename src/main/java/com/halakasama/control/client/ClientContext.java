package com.halakasama.control.client;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.server.QRNG;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by admin on 2017/3/29.
 */
public class ClientContext implements LocalContextHelper{
    private String clientUid;
    private QRNG qrng;
    private ConnectContext connectContext;



    @Override
    public String getLocalUid() {
        return clientUid;
    }

    @Override
    public boolean checkUidValid(String uid) {
        return StringUtils.equals(uid, GlobalParam.SERVER_UID);
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
        this.connectContext = connectContext;
    }

    @Override
    public void unregisterConnection(ConnectContext connectContext) {
        this.connectContext = null;
    }
}
