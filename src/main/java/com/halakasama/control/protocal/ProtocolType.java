package com.halakasama.control.protocal;

/**
 * Created by admin on 2017/3/28.
 */
public class ProtocolType {
    public static final short AuthProtocol = 0;
    public static final short KeySyncProtocol = 1;
    public static final short HeartBeatProtocol = 2;
    public static final short ProtocolNum = 3;

    public static boolean isAuthProtocol(short protocolType){
        return protocolType == AuthProtocol;
    }
    public static boolean isKeySyncProtocol(short protocolType){
        return protocolType == KeySyncProtocol;
    }
    public static boolean isHeartBeatProtocol(short protocolType){
        return protocolType == HeartBeatProtocol;
    }
    public static boolean isValidProtocol(short protocolType){
        return protocolType < ProtocolNum && protocolType >= 0;
    }
}
