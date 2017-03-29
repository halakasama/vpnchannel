package com.halakasama.control.server;

import java.util.Random;

/**
 * Created by admin on 2017/3/29.
 */
public class QRNG {
    private static QRNG ourInstance = new QRNG();

    public static QRNG getInstance() {
        return ourInstance;
    }

    private QRNG() {
    }

    synchronized public byte[] getRandomBytes(int num){
        byte[] randBytes = new byte[num];
        new Random().nextBytes(randBytes);
        return randBytes;
    }
}
