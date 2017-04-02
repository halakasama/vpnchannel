package com.halakasama.control;

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
    byte[] getCurrentKey(String uid, int keyPtr, int size);

    //SocketChannel连接维护
    void registerConnection(ConnectContext connectContext, String virtualAddress);
    void unregisterConnection(ConnectContext connectContext);


}
