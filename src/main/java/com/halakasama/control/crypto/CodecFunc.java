package com.halakasama.control.crypto;

/**
 * Created by admin on 2017/3/30.
 */
public interface CodecFunc {
    byte[] encode(byte[] msg, byte[] key);
    byte[] decode(byte[] msg, byte[] key);
}
