package com.halakasama.control.protocal.authentication.callee;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.ProtocolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/3/29.
 */
public class AuthCallee extends ProtocolHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCallee.class);
    AuthCalleeState currentState;
    byte[] challengeCode;

    public AuthCallee(ConnectContext connectContext, LocalContextHelper localContextHelper) {
        super(connectContext, localContextHelper);
        setInitialState();
    }

    @Override
    public void setInitialState() {
        currentState = AuthRequestWait.getInstance();
    }

    @Override
    public void handle(Message message) {
        if ( !ProtocolType.isAuthProtocolCaller(message.protocolType) ){
            if ( currentState instanceof AuthCalleeSuccess ){
                successor.handle(message);
            }else {
                LOGGER.warn("Inappropriate message received. {}", message);
            }
            return;
        }

        currentState.proceed(message,this);
        LOGGER.info("Current state is {}",currentState.getClass().getSimpleName());
    }

    @Override
    public void trigger() {

    }
}
