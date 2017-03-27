package com.halakasama.control.protocal;

import com.halakasama.control.ConnectContext;

/**
 * Created by admin on 2017/3/27.
 */
public abstract class ProtocolHandler {
    protected ProtocolHandler succesor;
    protected ProtoState currentState;
    protected ConnectContext connectContext;
    protected boolean canPassOn;

    public ProtocolHandler(ConnectContext connectContext) {
        this.connectContext = connectContext;
        canPassOn = false;
    }

    public abstract void initHandler();
    public abstract void handle();

    public ProtocolHandler setSuccesor(ProtocolHandler succesor) {
        this.succesor = succesor;
        return succesor;
    }
}
