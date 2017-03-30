package com.halakasama.control.crypto;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by admin on 2017/3/30.
 */
public class AESCodec implements CodecFunc {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESCodec.class);
    private static final AESCodec INSTANCE = new AESCodec();
    public static AESCodec getInstance(){
        return INSTANCE;
    }

    private AESCodec(){
    }

    /**
     *
     * @param msg
     * @param key 128位，即16字节
     * @return
     */
    @Override
    public byte[] encode(byte[] msg, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key,"AES"));
            byte[] cipherText = cipher.doFinal(msg);
            return cipherText;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            LOGGER.error("Encode error occurred.Message not encoded.",e);
        }
        return msg;
    }

    @Override
    public byte[] decode(byte[] cipherText, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(key,"AES"));
            byte[] message = cipher.doFinal(cipherText);
            return message;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            LOGGER.error("Encode error occurred.Message not encoded.",e);
        }
        return cipherText;
    }

    public static void main(String[] args) {
        AESCodec aesCodec = AESCodec.INSTANCE;
        byte[] key = new byte[16];
        byte[] message = StringUtils.getBytesUtf8("abcdefghijklmnopqrstuvwxyz");
        byte[] cipherText = aesCodec.encode(message,key);
        System.out.println(Arrays.toString(cipherText));
        byte[] decipherText = aesCodec.decode(cipherText,key);
        System.out.println(StringUtils.newStringUtf8(decipherText));
    }
}
