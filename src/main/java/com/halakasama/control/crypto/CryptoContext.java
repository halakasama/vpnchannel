package com.halakasama.control.crypto;

import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.crypto.*;
import com.halakasama.keymanage.CipherPair;
import com.halakasama.keymanage.KeyPair;

import java.util.Arrays;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class CryptoContext {
    public static final int ENCODE_ZONE = 0;
    public static final int DECODE_ZONE = 1;
    public static final int HMAC_SEND_ZONE = 2;
    public static final int HMAC_RECV_ZONE = 3;

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

    public int getHmacLength(){
        return hashFunc.getHmacLength();
    }
    public CipherPair getHmac(byte[] content){
        int saltSize = hashFunc.saltSizeInByte();
        KeyPair keyPair = localContextHelper.getCurrentKey(remoteUid, saltSize, HMAC_SEND_ZONE);
        byte[] hmac = hashFunc.getHmac(content, keyPair.key);
        return new CipherPair(keyPair.keyPtr, hmac);
    }

    public boolean checkHmacGood(byte[] stdHmac, byte[] content, int keyPtr){
        int saltSize = hashFunc.saltSizeInByte();
        byte[] salt = localContextHelper.getSpecifiedKey(remoteUid,keyPtr,saltSize, HMAC_RECV_ZONE);
        return Arrays.equals(stdHmac, hashFunc.getHmac(content, salt));
    }

    public byte[] decode(byte[] content, int keyPtr){
        int keySize = codecFunc.keySizeInByte();
        byte[] key = localContextHelper.getSpecifiedKey(remoteUid,keyPtr,keySize,DECODE_ZONE);
        return codecFunc.decode(content, key);
    }

    public CipherPair encode(byte[] content){
        int keySize = codecFunc.keySizeInByte();
        KeyPair keyPair = localContextHelper.getCurrentKey(remoteUid, keySize, ENCODE_ZONE);
        byte[] cipherText = codecFunc.encode(content, keyPair.key);
        return new CipherPair(keyPair.keyPtr, cipherText);
    }



    public String getRemoteUid() {
        return remoteUid;
    }

    public void setRemoteUid(String remoteUid) {
        this.remoteUid = remoteUid;
    }
}
