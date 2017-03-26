package com.halakasama.control;

import com.halakasama.control.protocal.ProtocolManager;

import java.nio.channels.SocketChannel;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class ConnectContext {
    SocketChannel socketChannel;
    ProtocolManager protocolManager;
    CryptoContext cryptoContext;
}
