package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCalleeFail implements AuthCalleeState {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCalleeFail.class);
    private static AuthCalleeFail ourInstance = new AuthCalleeFail();

    public static AuthCalleeFail getInstance() {
        return ourInstance;
    }

    private AuthCalleeFail() {
    }

    @Override
    public void proceed(Message message, AuthCallee authCallee) {
        LOGGER.error("This line of code is currently unreachable. Please check your code.");
    }
}
