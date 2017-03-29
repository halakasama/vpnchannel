package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;

/**
 * Created by admin on 2017/3/28.
 */
public class NewAuthStart implements AuthCallerState{
    private static final Logger LOGGER = LoggerFactory.getLogger(NewAuthStart.class);
    private static NewAuthStart ourInstance = new NewAuthStart();

    public static NewAuthStart getInstance() {
        return ourInstance;
    }

    private NewAuthStart() {
    }

    @Override
    public void proceed(Message message, AuthCaller authCaller) {
        ConnectContext connectContext = authCaller.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
        LocalContextHelper localContextHelper = connectContext.getLocalContextHelper();

        message = new Message(ProtocolType.AuthProtocol, AuthMessageType.AuthRequest,localContextHelper.getLocalId(),localContextHelper.getLocalId().length);
        if (!Message.sendMessage(socketChannel,message)){
            LOGGER.error("Message send error!");
            return;
        }
        authCaller.currentState = AuthRequestSent.getInstance();
    }
}
