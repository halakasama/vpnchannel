package com.halakasama.control.crypto;

/**
 * Created by admin on 2017/3/30.
 */
public interface HashFunc {
    byte[] getHmac(byte[] msg,byte[] salt);
    byte[] getEncryptHmac(byte[] msg, byte[] salt, byte[] key);
    int saltSizeInByte();
}
