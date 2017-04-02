package com.halakasama.control.protocal.config;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.ProtocolType;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class ConfigWait extends ProtocolHandler {
    ConfigWaitState currentState;
    public ConfigWait(ConnectContext connectContext, LocalContextHelper localContextHelper) {
        super(connectContext, localContextHelper);
        setInitialState();
    }

    @Override
    public void setInitialState() {
        currentState = WaitState.getInstance();
    }

    @Override
    public void handle(Message message) {
        if (!ProtocolType.isConfigProtocolPush(message.protocolType)){
            successor.handle(message);
            return;
        }
        currentState.proceed(message, this);
    }

    @Override
    public void trigger() {
    }
}
