package com.halakasama.control.server;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Created by admin on 2017/3/29.
 */
public class ServerContextHelper implements LocalContextHelper{
    ServerContext serverContext;

    /**
     * 获取本地UidBytes
     * @return
     */
    @Override
    public byte[] getLocalId() {
        return serverContext.serverUidBytes;
    }

    /**
     * 检查uid是否注册
     * @param uid
     * @return
     */
    @Override
    public boolean checkUidValid(String uid) {
        return true;
    }

    /**
     * 检查用户uid是否存在，并返回挑战码；如果未注册，则返回null
     * @return
     */
    @Override
    public byte[] getRandomBytes(int size) {
        return serverContext.qrng.getRandomBytes(size);
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
