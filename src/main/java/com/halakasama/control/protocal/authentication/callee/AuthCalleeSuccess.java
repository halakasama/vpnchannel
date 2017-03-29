package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.protocal.Message;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCalleeSuccess implements AuthCalleeState {
    private static AuthCalleeSuccess ourInstance = new AuthCalleeSuccess();

    public static AuthCalleeSuccess getInstance() {
        return ourInstance;
    }

    private AuthCalleeSuccess() {
    }

    @Override
    public void proceed(Message message, AuthCallee authCallee) {

    }
}
