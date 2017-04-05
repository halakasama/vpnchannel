package com.halakasama.control;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.authentication.callee.AuthCallee;
import com.halakasama.control.protocal.authentication.caller.AuthCaller;
import com.halakasama.control.protocal.config.ConfigPush;
import com.halakasama.control.protocal.config.ConfigWait;
import com.halakasama.control.protocal.defaulthandler.DefaultHandler;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class ConnectContext {
    private SocketChannel socketChannel; //当前连接专有的控制通道
    private CryptoContext cryptoContext; //当前连接专有的密码学参数
    private ProtocolHandler protocolHandlerChain; //当前连接专有的协议处理链
    private String remoteUid; //用户id
    private String virtualAddress;

    private ConnectContext() {
    }

    public static class Builder {
        private ConnectContext connectContext;

        public Builder(boolean serverMode, SocketChannel socketChannel, LocalContextHelper localContextHelper) {
            connectContext = new ConnectContext();
            connectContext.socketChannel = socketChannel;
            connectContext.cryptoContext = new CryptoContext(localContextHelper);

            //初始化protocolHandlerChain及remoteUid
            if (serverMode){
                connectContext.protocolHandlerChain = new AuthCallee(connectContext, localContextHelper);
                connectContext.protocolHandlerChain
                        .chainAddSuccessor(new AuthCaller(connectContext,localContextHelper))
                        .chainAddSuccessor(new ConfigPush(connectContext,localContextHelper))
                        .chainAddSuccessor(new DefaultHandler(connectContext,localContextHelper))
                ;
                connectContext.setRemoteUid(null);
            }else {
                connectContext.protocolHandlerChain = new AuthCaller(connectContext, localContextHelper);
                connectContext.protocolHandlerChain
                        .chainAddSuccessor(new AuthCallee(connectContext,localContextHelper))
                        .chainAddSuccessor(new ConfigWait(connectContext,localContextHelper))
                        .chainAddSuccessor(new DefaultHandler(connectContext,localContextHelper))
                ;
                connectContext.setRemoteUid(GlobalParam.SERVER_UID);
            }
        }

        public ConnectContext build() {
            return this.connectContext;
        }
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

    public String getRemoteUid() {
        return remoteUid;
    }

    public void setRemoteUid(String remoteUid) {
        this.remoteUid = remoteUid;
        cryptoContext.setRemoteUid(remoteUid);
    }

    public InetAddress getRemotePhysicalAddress(){
        return socketChannel.socket().getInetAddress();
    }
    public int getRemotePort(){
        return socketChannel.socket().getPort();
    }


    public String getVirtualAddress() {
        return virtualAddress;
    }

    public void setVirtualAddress(String virtualAddress) {
        this.virtualAddress = virtualAddress;
    }
}
