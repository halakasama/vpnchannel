package com.halakasama.control;

import com.halakasama.control.crypto.*;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class CryptoContext {
    private String remoteUid;
    private HashFunc hashFunc;
    private CodecFunc codecFunc;
    private LocalContextHelper localContextHelper;

    public CryptoContext(LocalContextHelper localContextHelper){
        hashFunc = Md5Hmac.getInstance();
        codecFunc = AESCodec.getInstance();
        remoteUid = null;
        this.localContextHelper = localContextHelper;
    }

    public byte[] calcHmac(byte[] content, int keyPtr){
        int saltSize = hashFunc.saltSizeInByte();
        byte[] salt = localContextHelper.getCurrentKey(remoteUid,keyPtr,saltSize);
        return hashFunc.getHmac(content, salt);
    }

    public String getRemoteUid() {
        return remoteUid;
    }

    public void setRemoteUid(String remoteUid) {
        this.remoteUid = remoteUid;
    }
}
