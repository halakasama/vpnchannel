package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.apache.commons.codec.binary.StringUtils;
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

    /**
     * 发送uid，发起认证请求
     * @param message
     * @param authCaller
     */
    @Override
    public void proceed(Message message, AuthCaller authCaller) {
        ConnectContext connectContext = authCaller.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
        LocalContextHelper localContextHelper = authCaller.getLocalContextHelper();

        //发送挑战应答认证请求
        byte[] uidInByte = StringUtils.getBytesUtf8(localContextHelper.getLocalUid());
        Message.sendMessage(socketChannel,new Message(ProtocolType.AuthProtocolCaller, AuthMessageType.AuthRequest,uidInByte,uidInByte.length));

        //更新状态机
        authCaller.currentState = AuthRequestSent.getInstance();
    }
}
