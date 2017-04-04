package com.halakasama.client;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.keymanage.KeyPair;
import com.halakasama.server.QRNG;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by admin on 2017/3/29.
 */
public class ClientContext implements LocalContextHelper{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientContext.class);
    private InetAddress serverAddress;
    private InetAddress clientAddress;
    private final int serverUdpPort;
    private final int clientUdpPort;

    private String clientUid;
    private String virtualAddress;
    private QRNG qrng;
    private ConnectContext connectContext;

    public ClientContext(String clientUid, String serverAddress, String clientAddress, int serverUdpPort, int clientUdpPort){
        qrng = QRNG.getInstance();
        this.clientUid = clientUid;
        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
            this.clientAddress = InetAddress.getByName(clientAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Construct failed.", e);
        }
        this.serverUdpPort = serverUdpPort;
        this.clientUdpPort = clientUdpPort;
    }

    public CryptoContext getCryptoContext(){
        return connectContext.getCryptoContext();
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
    public byte[] getSpecifiedKey(String uid, int keyPtr, int size, int zone) {
        return new byte[size];
    }

    @Override
    public KeyPair getCurrentKey(String uid, int size, int zone) {
        return new KeyPair(0,new byte[size]);
    }

    @Override
    public int getCurrentKeyPtr(String uid, int zone) {
        return 0;
    }

    @Override
    public void registerConnection(ConnectContext connectContext, String virtualAddress) {
        this.connectContext = connectContext;
        this.virtualAddress = virtualAddress;
        LOGGER.info("Server {} connected.", connectContext.getRemoteUid());

        ClientDataChannel.startDataChannel(serverAddress,clientAddress,serverUdpPort,clientUdpPort,virtualAddress,this);
        LOGGER.info("Data channel start.");
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
