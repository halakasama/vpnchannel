package com.halakasama.keymanage;

/**
 * Created by pengfei.ren on 2017/4/4.
 */
public class CipherPair {
    public final int keyPtr;
    public final byte[] cipherText;
    public final int size;

    public CipherPair(int keyPtr, byte[] cipherText) {
        this.keyPtr = keyPtr;
        this.cipherText = cipherText;
        this.size = cipherText.length;
    }
}
