package com.halakasama.control;

import com.halakasama.control.protocal.ProtocolHandler;

import java.nio.channels.SocketChannel;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class ConnectContext {
    private SocketChannel socketChannel; //当前连接专有的控制通道
    private CryptoContext cryptoContext; //当前连接专有的密码学参数
    private LocalContextHelper localContextHelper;
    private ProtocolHandler protocolHandlerChain; //当前连接专有的协议处理链
    private String uid; //用户id

    public LocalContextHelper getLocalContextHelper() {
        return localContextHelper;
    }

    public void setLocalContextHelper(LocalContextHelper localContextHelper) {
        this.localContextHelper = localContextHelper;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public ProtocolHandler getProtocolHandlerChain() {
        return protocolHandlerChain;
    }

    public void setProtocolHandlerChain(ProtocolHandler protocolHandlerChain) {
        this.protocolHandlerChain = protocolHandlerChain;
    }

    public CryptoContext getCryptoContext() {
        return cryptoContext;
    }

    public void setCryptoContext(CryptoContext cryptoContext) {
        this.cryptoContext = cryptoContext;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
