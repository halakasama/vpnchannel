package com.halakasama.control.protocal;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;

/**
 * Created by admin on 2017/3/27.
 */
public abstract class ProtocolHandler {
    protected ProtocolHandler successor;
    protected ConnectContext connectContext;

    public ProtocolHandler(ConnectContext connectContext) {
        this.connectContext = connectContext;
    }

    public abstract void setInitialState();
    public abstract void handle(Message message);

    public ProtocolHandler chainAddSuccessor(ProtocolHandler successor) {
        this.successor = successor;
        return successor;
    }

    public void setSuccessor(ProtocolHandler successor) {
        this.successor = successor;
    }

    public void setConnectContext(ConnectContext connectContext) {
        this.connectContext = connectContext;
    }

    public ProtocolHandler getSuccessor() {
        return successor;
    }

    public ConnectContext getConnectContext() {
        return connectContext;
    }
}
