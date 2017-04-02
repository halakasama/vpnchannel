package com.halakasama.control.protocal;

/**
 * Created by admin on 2017/3/28.
 */
public class ProtocolType {
    public static final short AuthProtocolCaller = 0;
    public static final short AuthProtocolCallee = 1;
    public static final short ConfigProtocolPush = 2;
    public static final short ConfigProtocolWait = 3;
    public static final short KeySyncProtocol = -1;
    public static final short HeartBeatProtocol = -1;
    public static final short ProtocolNum = 4;

    public static boolean isAuthProtocolCaller(short protocolType){
        return protocolType == AuthProtocolCaller;
    }
    public static boolean isAuthProtocolCallee(short protocolType){
        return protocolType == AuthProtocolCallee;
    }
    public static boolean isConfigProtocolPush(short protocolType){
        return protocolType == ConfigProtocolPush;
    }
    public static boolean isConfigProtocolWait(short protocolType){
        return protocolType == ConfigProtocolWait;
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
