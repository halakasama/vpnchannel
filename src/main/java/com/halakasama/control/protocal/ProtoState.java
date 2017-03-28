package com.halakasama.control.protocal;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public interface ProtoState {
    void proceed(Message message, ProtocolHandler protocolHandler);
}
