package com.halakasama.control.protocal.authentication;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.ServerContext;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.ProtocolType;

/**
 * Created by admin on 2017/3/28.
 */
public class AuthCallee extends ProtocolHandler{
    private boolean authPassed;
    public AuthCallee(ConnectContext connectContext) {
        super(connectContext);
        authPassed = false;
    }

    @Override
    public void setInitialState() {
        currentState = NewAuthState.getInstance();
    }

    @Override
    public void handle(Message message) {
        if ( !ProtocolType.isAuthProtocol(message.protocolType) && authPassed){
            successor.handle(message);
            return;
        }
        currentState.proceed(message,this);
    }
}
