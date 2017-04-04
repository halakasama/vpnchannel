package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthRequestSent implements AuthCallerState {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRequestSent.class);
    private static AuthRequestSent ourInstance = new AuthRequestSent();

    public static AuthRequestSent getInstance() {
        return ourInstance;
    }

    private AuthRequestSent() {
    }

    /**
     * 已发送认证请求，收到challenge code后， 使用challenge code 和 相应秘钥 计算hmac，并发送应答消息
     * @param message
     * @param authCaller
     */
    @Override
    public void proceed(Message message, AuthCaller authCaller) {
        ConnectContext connectContext = authCaller.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
        CryptoContext cryptoContext = connectContext.getCryptoContext();

        //检查消息类型是否合法
        if (!AuthMessageType.isChallengeCode(message.msgType)){
            LOGGER.error("Wrong message received. {}",message);
            return;
        }

        //计算挑战码hmac，作为应答消息
        byte[] challengeCode = Arrays.copyOf(message.content,message.contentLen);
        byte[] challengeReply = cryptoContext.getHmac(challengeCode).cipherText;

        //发送应答码消息
        Message.sendMessage(socketChannel, new Message(ProtocolType.AuthProtocolCaller,AuthMessageType.ChallengeResponse,challengeReply,challengeReply.length));

        //更新状态机
        authCaller.currentState = ChallengeResponseSent.getInstance();
    }
}
