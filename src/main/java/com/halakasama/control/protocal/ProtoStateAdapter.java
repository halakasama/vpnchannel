package com.halakasama.control.protocal;

import com.halakasama.control.ConnectContext;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public abstract class ProtoStateAdapter implements ProtoState {
    protected ProtocolManager protocolManager;
    protected ConnectContext connectContext;

    public ProtoStateAdapter(ConnectContext connectContext) {
        this.connectContext = connectContext;
    }

    @Override
    public abstract void proceed();
}
