package com.halakasama.control.protocal.negotiation;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolHandler;

/**
 * Created by admin on 2017/3/28.
 */
public class NegotiationCaller extends ProtocolHandler {
    public NegotiationCaller(ConnectContext connectContext, LocalContextHelper localContextHelper) {
        super(connectContext,localContextHelper);
    }

    @Override
    public void setInitialState() {

    }

    @Override
    public void handle(Message message) {

    }

    @Override
    public void trigger() {

    }
}
