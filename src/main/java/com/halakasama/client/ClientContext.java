package com.halakasama.client;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.server.QRNG;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by admin on 2017/3/29.
 */
public class ClientContext implements LocalContextHelper{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientContext.class);

    private String clientUid;
    private String virtualAddress;
    private QRNG qrng;
    private ConnectContext connectContext;

    public ClientContext(String clientUid){
        qrng = QRNG.getInstance();
        this.clientUid = clientUid;
    }


    @Override
    public String getLocalUid() {
        return clientUid;
    }

    @Override
    public boolean checkUidValid(String uid) {
        return StringUtils.equals(uid, GlobalParam.SERVER_UID);
    }

    @Override
    public byte[] getRandomBytes(int size) {
        return qrng.getRandomBytes(size);
    }

    @Override
    public byte[] getCurrentKey(String uid, int keyPtr, int size) {
        return new byte[size];
    }

    @Override
    public void registerConnection(ConnectContext connectContext, String virtualAddress) {
        this.connectContext = connectContext;
        this.virtualAddress = virtualAddress;
//        connectContext.setVirtualAddress(virtualAddress);
        LOGGER.info("Server {} connected.", connectContext.getRemoteUid());
    }

    @Override
    public void unregisterConnection(ConnectContext connectContext) {
        LOGGER.info("Server {} connection disconnected.", connectContext.getRemoteUid());
        try {
            connectContext.getSocketChannel().close();
        } catch (IOException e) {
            LOGGER.error("SocketChannel close failed.",e);
        }
        this.connectContext = null;
    }
}
