package com.halakasama.control.protocal;

import com.halakasama.control.ConnectContext;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class ProtocolManager {
    ConnectContext connectContext;
    ProtoState currentState;
    public void handle(byte[] message){
        if (isProtoInitRequest(message)){

        }
    }

    boolean isProtoInitRequest(byte[] message){
        return false;
    }
}
