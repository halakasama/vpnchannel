package com.halakasama.control;

import com.halakasama.control.crypto.*;
import com.halakasama.control.server.QRNG;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class CryptoContext {
    public long keyPtr;
    public HashFunc hashFunc;
    public CodecFunc codecFunc;

    public CryptoContext(){
        keyPtr = 2;
        hashFunc = Md5Hmac.getInstance();
        codecFunc = AESCodec.getInstance();
    }


    @Override
    public byte[] getHmac(byte[] msg, byte[] salt) {
        return hashFunc.getHmac(msg,salt);
    }

    @Override
    public byte[] getEncryptHmac(byte[] msg, byte[] salt, byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] encode(byte[] msg, byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] decode(byte[] msg, byte[] key) {
        return new byte[0];
    }
}
