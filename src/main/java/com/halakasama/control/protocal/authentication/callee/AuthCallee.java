package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.ProtocolType;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCallee extends ProtocolHandler {

    AuthCalleeState currentState;
    byte[] challengeCode;

    public AuthCallee(ConnectContext connectContext, LocalContextHelper localContextHelper) {
        super(connectContext, localContextHelper);
    }

    @Override
    public void setInitialState() {
        currentState = AuthRequestWait.getInstance();
    }

    @Override
    public void handle(Message message) {
        if ( !ProtocolType.isAuthProtocol(message.protocolType) && currentState instanceof AuthCalleeSuccess){
            successor.handle(message);
            return;
        }
        currentState.proceed(message,this);
    }
}
