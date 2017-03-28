package com.halakasama.control.protocal.authentication;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.protocal.Message;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class AuthResultRecv extends ProtoStateAdapter {

    public AuthResultRecv(ConnectContext connectContext) {
        super(connectContext);
    }

    @Override
    public void proceed(Message message) {

    }
}
