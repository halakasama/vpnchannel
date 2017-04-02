package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.Message;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCallerSuccess implements AuthCallerState {
    private static AuthCallerSuccess ourInstance = new AuthCallerSuccess();

    public static AuthCallerSuccess getInstance() {
        return ourInstance;
    }

    private AuthCallerSuccess() {
    }

    @Override
    public void proceed(Message message, AuthCaller authCaller) {

    }
}
