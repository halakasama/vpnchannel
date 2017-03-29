package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.protocal.Message;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCalleeFail implements AuthCalleeState {
    private static AuthCalleeFail ourInstance = new AuthCalleeFail();

    public static AuthCalleeFail getInstance() {
        return ourInstance;
    }

    private AuthCalleeFail() {
    }

    @Override
    public void proceed(Message message, AuthCallee authCallee) {

    }
}
