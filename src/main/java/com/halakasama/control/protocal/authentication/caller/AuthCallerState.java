package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.Message;

/**
 * Created by admin on 2017/3/29.
 */
public interface AuthCallerState {
    void proceed(Message message, AuthCaller authCaller);
}
