package com.halakasama.server;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.ConnectContext;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.keymanage.KeyPair;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2017/3/28.
 */
public class ServerContext implements LocalContextHelper{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerContext.class);
    private String serverUid;
    private QRNG qrng;
    private Map<String,ConnectContext> addressContextMap;

    public ServerContext(){
        serverUid = GlobalParam.SERVER_UID;
        qrng = QRNG.getInstance();
        addressContextMap = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        byte[] bytes = StringUtils.getBytesUtf8("abcde大");
        String string = StringUtils.newStringUtf8(bytes);
        System.out.println(string);
    }

    public boolean isVirtualAddressValid(String virtualAddress){
        return addressContextMap.containsKey(virtualAddress);
    }
    public InetAddress getPhysicalAddress(String virtualAddress){
        ConnectContext connectContext = addressContextMap.get(virtualAddress);
        if (connectContext == null){
            LOGGER.error("Virtual address {} not registered yet.",virtualAddress);
            return null;
        }
        return connectContext.getRemotePhysicalAddress();
    }
    public CryptoContext getCryptoContext(String virtualAddress){
        ConnectContext connectContext = addressContextMap.get(virtualAddress);
        if (connectContext == null){
            LOGGER.error("Virtual address {} not registered yet.",virtualAddress);
            return null;
        }
        return connectContext.getCryptoContext();
    }

    public byte[] fakeRandom(int size){
        byte[] key = new byte[size];
        Arrays.fill(key, (byte) 1);
        return key;
    }

    @Override
    public String getLocalUid() {
        return serverUid;
    }

    @Override
    public boolean checkUidValid(String uid) {
        return true;
    }

    @Override
    public byte[] getRandomBytes(int size) {
        return qrng.getRandomBytes(size);
    }

    @Override
    public byte[] getSpecifiedKey(String uid, int keyPtr, int size, int zone) {
        return fakeRandom(size);
    }

    @Override
    public KeyPair getCurrentKey(String uid, int size, int zone) {
        return new KeyPair(0,fakeRandom(size));
    }

    @Override
    public int getCurrentKeyPtr(String uid, int zone) {
        return 0;
    }

    @Override
    public void registerConnection(ConnectContext connectContext, String virtualAddress) {
        addressContextMap.put(virtualAddress,connectContext);
        connectContext.setVirtualAddress(virtualAddress);
        LOGGER.info("User {} log in. Virtual address is {}. Physical address is {}.", connectContext.getRemoteUid(), virtualAddress, connectContext.getRemotePhysicalAddress());
    }

    @Override
    public void unregisterConnection(ConnectContext connectContext) {
        String virtualAddress = connectContext.getVirtualAddress();
        if (virtualAddress != null && addressContextMap.containsKey(virtualAddress)) {
            addressContextMap.remove(virtualAddress);
            VirtualAddressManager.getInstance().removeVirtualAddress(virtualAddress);
        }
        LOGGER.info("User {} log out.", connectContext.getRemoteUid());
        try {
            connectContext.getSocketChannel().socket().close();
            connectContext.getSocketChannel().close();
        } catch (IOException e) {
            LOGGER.error("SocketChannel close failed.",e);
        }
    }
}
