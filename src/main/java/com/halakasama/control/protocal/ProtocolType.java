package com.halakasama.control.protocal;

/**
 * Created by admin on 2017/3/28.
 */
public class ProtocolType {
    public static final int AuthProtocol = 0;
    public static final int KeySyncProtocol = 1;
    public static final int HeartBeatProtocol = 2;
    public static final int ProtocolNum = 3;

    public static boolean isAuthProtocol(int protocolType){
        return protocolType == AuthProtocol;
    }
    public static boolean isKeySyncProtocol(int protocolType){
        return protocolType == KeySyncProtocol;
    }
    public static boolean isHeartBeatProtocol(int protocolType){
        return protocolType == HeartBeatProtocol;
    }
    public static boolean isValidProtocol(int protocolType){
        return protocolType < ProtocolNum && protocolType >= 0;
    }
}
