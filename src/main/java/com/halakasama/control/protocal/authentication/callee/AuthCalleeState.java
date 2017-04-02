package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.Message;

/**
 * Created by admin on 2017/3/29.
 */
public interface AuthCalleeState {
    void proceed(Message message, AuthCallee authCallee);
}
