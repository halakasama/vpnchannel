package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthRequestWait implements AuthCalleeState{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRequestWait.class);

    private static AuthRequestWait ourInstance = new AuthRequestWait();

    public static AuthRequestWait getInstance() {
        return ourInstance;
    }

    private AuthRequestWait() {
    }

    /**
     * 收到认证请求，并发送挑战码
     * @param message
     * @param authCallee 为认证方，AuthCallee
     */
    @Override
    public void proceed(Message message, AuthCallee authCallee) {
        ConnectContext connectContext = authCallee.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
        LocalContextHelper localContextHelper = authCallee.getLocalContextHelper();

        //检查消息类型是否合法
        if (!AuthMessageType.isAuthRequest(message.msgType)){
            LOGGER.error("Wrong message received. {}",message);
            return;
        }

        //首先验证uid是否有效；无效则drop消息，并关闭SocketChannel
        String uid = StringUtils.newStringUtf8(Arrays.copyOf(message.content,message.contentLen));
        connectContext.setRemoteUid(uid);
        if (!localContextHelper.checkUidValid(uid)){
            LOGGER.error("Invalid uid {}", uid);
            try {
                socketChannel.close();
            } catch (IOException e) {
                LOGGER.error("SocketChannel close failed.",e);
            }
            return;
        }

        //获取挑战随机数，并保存在AuthCallee协议状态机中
        byte[] challengeCode = localContextHelper.getRandomBytes(GlobalParam.CHALLENGE_CODE_SIZE);
        authCallee.challengeCode = challengeCode;

        //将挑战随机数封装到Message，并发送
        Message.sendMessage(socketChannel, new Message(ProtocolType.AuthProtocolCallee,AuthMessageType.ChallengeCode,challengeCode,challengeCode.length));

        //更新状态机状态
        authCallee.currentState = ChallengeCodeSent.getInstance();
    }
}
