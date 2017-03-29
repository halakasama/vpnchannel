package com.halakasama.control;

import com.halakasama.control.protocal.Message;

/**
 * Created by admin on 2017/3/29.
 */
public interface LocalContextHelper {
    byte[] getLocalId();
    boolean checkUidValid(String uidBytes);
    byte[] getRandomBytes(int size);
    byte[] getChallengeResponse(ConnectContext connectContext, Message message);
    byte getAuthResult(ConnectContext connectContext, Message message);
    boolean handleAuthResult(ConnectContext connectContext, Message message);
}
