package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Created by admin on 2017/3/29.
 */
public class ChallengeCodeSent implements AuthCalleeState{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeCodeSent.class);

    private static ChallengeCodeSent ourInstance = new ChallengeCodeSent();

    public static ChallengeCodeSent getInstance() {
        return ourInstance;
    }

    private ChallengeCodeSent() {
    }

    /**
     * 收到挑战码应答消息，判断认证是否成功
     * @param message 挑战码应答消息
     * @param authCallee 为挑战认证方AuthCallee
     */
    @Override
    public void proceed(Message message, AuthCallee authCallee) {
        ConnectContext connectContext = authCallee.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
        LocalContextHelper localContextHelper = connectContext.getLocalContextHelper();

        //检查消息类型是否合法
        if (!AuthMessageType.isChallengeResponse(message.msgType)){
            LOGGER.error("Wrong message received. {}",message);
            return;
        }

        //检查应答消息是否有效
        String uid = connectContext.getUid();
//        int keyPtr =
//        byte[] sharedKey = localContextHelper.getSharedKey(uid,);
//        byte[] codeHmac =
        byte authResult = localContextHelper.getAuthResult(connectContext,message);



        //发送认证结果
        Message.sendMessage(socketChannel, new Message(ProtocolType.AuthProtocol,AuthMessageType.AuthResult,new byte[]{authResult},1));

        //根据认证结果更新状态机，如果认证失败，则关闭SocketChannel
        if (authResult == 0){
            authCallee.currentState = AuthCalleeSuccess.getInstance();
            LOGGER.info("Authentication success! uid = {}, {}:{}",uid,socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort());
        }else {
            authCallee.currentState = AuthCalleeFail.getInstance();
            LOGGER.info("Authentication failed! uid = {}, {}:{}",uid,socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort());
            try {
                socketChannel.close();
            } catch (IOException e) {
                LOGGER.error("SocketChannel close failed.",e);
            }
        }

    }
}
