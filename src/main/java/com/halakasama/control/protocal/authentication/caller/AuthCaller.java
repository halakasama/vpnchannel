package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.control.protocal.authentication.callee.AuthCalleeSuccess;

/**
 * Created by admin on 2017/3/28.
 */
public class AuthCaller extends ProtocolHandler{

    AuthCallerState currentState;
    public AuthCaller(ConnectContext connectContext) {
        super(connectContext);
    }

    @Override
    public void setInitialState() {
        currentState = NewAuthStart.getInstance();
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
