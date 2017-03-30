package com.halakasama.control.client;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;

/**
 * Created by admin on 2017/3/29.
 */
public class ClientContextHelper implements LocalContextHelper {
    @Override
    public byte[] getLocalId() {
        return new byte[0];
    }

    @Override
    public boolean checkUidValid(String uidBytes) {
        return false;
    }

    @Override
    public byte[] getRandomBytes(int size) {
        return new byte[0];
    }

    @Override
    public byte[] getChallengeResponse(ConnectContext connectContext, Message message) {
        return new byte[0];
    }

    @Override
    public byte getAuthResult(ConnectContext connectContext, Message message) {
        return 0;
    }

    @Override
    public boolean handleAuthResult(ConnectContext connectContext, Message message) {
        return false;
    }
}
