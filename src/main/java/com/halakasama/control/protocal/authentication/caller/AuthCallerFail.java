package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.authentication.callee.AuthCalleeFail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCallerFail implements AuthCallerState {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCallerFail.class);
    private static AuthCallerFail ourInstance = new AuthCallerFail();

    public static AuthCallerFail getInstance() {
        return ourInstance;
    }

    private AuthCallerFail() {
    }

    @Override
    public void proceed(Message message, AuthCaller authCaller) {
        LOGGER.error("This line of code is currently unreachable. Please check your code.");
    }
}
