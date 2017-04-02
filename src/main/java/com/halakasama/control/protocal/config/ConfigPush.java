package com.halakasama.control.protocal.config;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolHandler;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class ConfigPush extends ProtocolHandler{

    ConfigPushState currentState;

    public ConfigPush(ConnectContext connectContext, LocalContextHelper localContextHelper) {
        super(connectContext, localContextHelper);
        setInitialState();
    }

    @Override
    public void setInitialState() {
        currentState = PushState.getInstance();
    }

    @Override
    public void handle(Message message) {
        successor.handle(message);
    }

    @Override
    public void trigger() {
        currentState.proceed(null, this);
    }
}
