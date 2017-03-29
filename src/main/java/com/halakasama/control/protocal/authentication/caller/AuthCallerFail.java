package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.protocal.Message;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCallerFail implements AuthCallerState {
    private static AuthCallerFail ourInstance = new AuthCallerFail();

    public static AuthCallerFail getInstance() {
        return ourInstance;
    }

    private AuthCallerFail() {
    }

    @Override
    public void proceed(Message message, AuthCaller authCaller) {

    }
}
