package com.halakasama.server;


import com.halakasama.control.crypto.RandGen;

import java.security.SecureRandom;

/**
 * Created by admin on 2017/3/29.
 */
public class QRNG implements RandGen{
    private static QRNG ourInstance = new QRNG();

    public static QRNG getInstance() {
        return ourInstance;
    }

    private QRNG() {
    }

    @Override
    synchronized public byte[] getRandomBytes(int num){
        byte[] randBytes = new byte[num];
        new SecureRandom().nextBytes(randBytes);
        return randBytes;
    }
}
