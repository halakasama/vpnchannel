package com.halakasama.vpn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * Created by pengfei.ren on 2017/3/14.
 */
public class ClientSocket{
    private final Logger LOGGER = LoggerFactory.getLogger(ClientSocket.class);

    public InetAddress physicalAddress;
    public String virtualAddress;
    public int clientPort;

    public ClientSocket(String physicalAddress, String virtualAddress, int clientPort) {
        this.virtualAddress = virtualAddress;
        this.clientPort = clientPort;
        try {
            this.physicalAddress = InetAddress.getByName(physicalAddress);
        } catch (Exception e) {
            LOGGER.error("Invalid physical ip address!",e);
        }
    }
}
