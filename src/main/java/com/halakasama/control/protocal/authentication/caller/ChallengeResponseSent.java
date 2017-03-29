package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.authentication.AuthMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/3/29.
 */
public class ChallengeResponseSent implements AuthCallerState {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeResponseSent.class);
    private static ChallengeResponseSent ourInstance = new ChallengeResponseSent();

    public static ChallengeResponseSent getInstance() {
        return ourInstance;
    }

    private ChallengeResponseSent() {
    }

    /**
     * 接收认证结果消息，判断认证是否成功
     * @param message 认证结果消息
     * @param authCaller  为认证发起者 AuthCaller
     */
    @Override
    public void proceed(Message message, AuthCaller authCaller) {
        ConnectContext connectContext = authCaller.getConnectContext();

        if (!AuthMessageType.isAuthResult(message.msgType)){
            LOGGER.error("Wrong message received. {}",message);
            return;
        }

        LocalContextHelper localContextHelper = connectContext.getLocalContextHelper();
        boolean authSuccess = localContextHelper.handleAuthResult(connectContext, message);
        if (authSuccess){
            authCaller.currentState = AuthCallerSuccess.getInstance();
            LOGGER.info("Authentication success!");
        }else {
            authCaller.currentState = AuthCallerFail.getInstance();
            LOGGER.info("Authentication failed!");

        }
    }
}
