package com.halakasama.control.protocal.authentication;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.protocal.ProtocolHandler;

/**
 * Created by admin on 2017/3/27.
 */
public class AuthCaller extends ProtocolHandler{

    public AuthCaller(ConnectContext connectContext) {
        super(connectContext);
    }

    @Override
    public void initHandler() {
        currentState = new NewAuthState(connectContext);
    }

    @Override
    public void handle() {

    }
}
