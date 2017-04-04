package com.halakasama.keymanage;

/**
 * Created by pengfei.ren on 2017/4/4.
 */
public class KeyPair {
    public final int keyPtr;
    public final byte[] key;
    public final int size;

    public KeyPair(int keyPtr, byte[] key) {
        this.keyPtr = keyPtr;
        this.key = key;
        size = key.length;
    }
}
