package com.halakasama.control.protocal.authentication;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtoState;
import com.halakasama.control.protocal.ProtocolHandler;

import java.nio.channels.SocketChannel;

/**
 * Created by admin on 2017/3/28.
 */
public class NewAuthState implements ProtoState{
    private static NewAuthState ourInstance = new NewAuthState();

    public static NewAuthState getInstance() {
        return ourInstance;
    }

    private NewAuthState() {
    }

    @Override
    public void proceed(Message message, ProtocolHandler protocolHandler) {
        ConnectContext connectContext = protocolHandler.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
    }
}
