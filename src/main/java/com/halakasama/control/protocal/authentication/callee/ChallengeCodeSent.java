package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.CryptoContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
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
        LocalContextHelper localContextHelper = authCallee.getLocalContextHelper();
        CryptoContext cryptoContext = connectContext.getCryptoContext();

        //检查消息类型是否合法
        if (!AuthMessageType.isChallengeResponse(message.msgType)){
            LOGGER.error("Wrong message received. {}",message);
            return;
        }

        //检查应答消息是否有效
        String uid = connectContext.getRemoteUid();
        byte[] challengeReply = message.content;
        byte[] stdReply = cryptoContext.calcHmac(authCallee.challengeCode, GlobalParam.AUTH_SALT_KEY_PTR);
        boolean authResult = ByteUtils.equals(challengeReply,stdReply);

        //发送认证结果
        Message.sendMessage(socketChannel, new Message(ProtocolType.AuthProtocol,AuthMessageType.AuthResult,new byte[]{(byte)(authResult ? 0 : 1)},1));

        //根据认证结果更新状态机，如果认证成功，则注册ConnectionContext;否则，关闭SocketChannel
        if (authResult){
            localContextHelper.registerConnection(connectContext);
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
