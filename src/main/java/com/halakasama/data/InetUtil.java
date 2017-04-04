package com.halakasama.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by pengfei.ren on 2017/4/4.
 */
public class InetUtil {
    public static String inetFromByteToString(byte[] address){
        try {
            InetAddress inetAddress = InetAddress.getByAddress(address);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "255.255.255.255";
        }
    }

    public static byte[] inetFromStringToByte(String address){
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            return inetAddress.getAddress();
        } catch (UnknownHostException e) {
            return new byte[]{-1,-1,-1,-1};
        }
    }
}
