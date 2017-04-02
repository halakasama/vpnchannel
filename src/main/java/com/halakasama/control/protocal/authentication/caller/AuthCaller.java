package com.halakasama.control.protocal.authentication.caller;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.protocal.ProtocolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/3/28.
 */
public class AuthCaller extends ProtocolHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCaller.class);
    AuthCallerState currentState;
    public AuthCaller(ConnectContext connectContext, LocalContextHelper localContextHelper) {
        super(connectContext,localContextHelper);
        setInitialState();
    }

    @Override
    public void setInitialState() {
        currentState = NewAuthStart.getInstance();
    }

    @Override
    public void handle(Message message) {
        if ( !ProtocolType.isAuthProtocolCallee(message.protocolType) ){
            if ( currentState instanceof AuthCallerSuccess ){
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
        if ( currentState instanceof NewAuthStart){
            currentState.proceed(null,this);
        }
    }
}
