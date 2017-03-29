package com.halakasama.control.protocal.defaulthandler;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/3/29.
 */
public class DefaultHandler extends ProtocolHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHandler.class);

    public DefaultHandler(ConnectContext connectContext) {
        super(connectContext);
    }

    @Override
    public void setInitialState() {

    }

    @Override
    public void handle(Message message) {
        LOGGER.error("Invalid message received. {}", message);
    }
}
