package com.halakasama.control.protocal.config;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class ConfigMessageType {
    public static final short VirtualAddress = 0;//虚拟地址
    public static final short ConfigMessageTypeNum = 1;

    public static boolean isVirtualAddress(short msgType){
        return VirtualAddress == msgType;
    }
}
