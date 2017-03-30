package com.halakasama.control.crypto;


import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by admin on 2017/3/30.
 */
public class Md5Hmac implements HashFunc {
    public static final Logger LOGGER = LoggerFactory.getLogger(Md5Hmac.class);
    private static final Md5Hmac INSTANCE = new Md5Hmac();
    private Md5Hmac(){}
    public static Md5Hmac getInstance(){
        return INSTANCE;
    }
    @Override
    public byte[] getHmac(byte[] msg, byte[] salt) {
        return HmacUtils.hmacMd5(salt,msg);
    }

    @Override
    public byte[] getEncryptHmac(byte[] msg, byte[] salt, byte[] key) {
        return getHmac(msg,salt);
    }

    public static void main(String[] args) {
        byte[] key = new byte[16];
        byte[] message = StringUtils.getBytesUtf8("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        HashFunc hashFunc = Md5Hmac.getInstance();
        byte[] hmac = hashFunc.getEncryptHmac(message,key,null);
        System.out.println(Arrays.toString(hmac));
        System.out.println(hmac.length);
    }
}
