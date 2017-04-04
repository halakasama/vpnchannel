package com.halakasama.control;

import com.halakasama.keymanage.KeyPair;

/**
 * Created by admin on 2017/3/29.
 */
public interface LocalContextHelper {
    //用户操作
    String getLocalUid();
    boolean checkUidValid(String uidBytes);

    //随机数发生器
    byte[] getRandomBytes(int size);

    //操作秘钥

    /**
     *
     * @param uid
     * @param keyPtr
     * @param size
     * @param zone
     *  0 encode zone
     *  1 decode zone
     *  2 hmac send zone
     *  3 hmac receive zone
     * @return
     */
    byte[] getSpecifiedKey(String uid, int keyPtr, int size, int zone);
    KeyPair getCurrentKey(String uid, int size, int zone);
    int getCurrentKeyPtr(String uid, int zone);

    //SocketChannel连接维护
    void registerConnection(ConnectContext connectContext, String virtualAddress);
    void unregisterConnection(ConnectContext connectContext);
}
