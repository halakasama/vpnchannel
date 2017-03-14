package com.halakasama.vpn;

import com.google.common.io.Resources;
import com.halakasama.config.Configuration;
import com.halakasama.config.ServerConfiguration;
import com.halakasama.packet.DataPacket;
import org.msgpack.MessagePack;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IllegalRawDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * Created by pengfei.ren on 2017/3/14.
 */
public class ForwardServer {
    private final Logger LOGGER = LoggerFactory.getLogger(ForwardServer.class);

    private final ServerConfiguration SERVER_CONFIG;

    public ForwardServer(String configPath) {
        SERVER_CONFIG = (ServerConfiguration) Configuration.getConfiguration(configPath,false,-1);
    }

    public void run() throws IOException, IllegalRawDataException {
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(SERVER_CONFIG.serverPort,SERVER_CONFIG.serverAddress);
        } catch (SocketException e) {
            LOGGER.error("Server socket build failed.",e);
        }
        byte[] buf = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        MessagePack msgPack = new MessagePack();
        while (null!=server){
            server.receive(packet);
            byte[] msg = packet.getData();
            LOGGER.info("Msg received. {}",msg);
            DataPacket dataPacket = null;
            try {
                dataPacket = msgPack.read(msg,DataPacket.class);
                LOGGER.info("Ethernet frame {}", EthernetPacket.newPacket(dataPacket.data,0,dataPacket.data.length));
            } catch (IOException e) {
                LOGGER.error("MessagePack read error!",e);
                continue;
            }
            String dstVirtualAddress = dataPacket.dstAddress;
            if (!SERVER_CONFIG.routeTable.containsKey(dstVirtualAddress)){
                LOGGER.info("VirtualAddress {} not registered yet!",dstVirtualAddress);
                continue;
            }
            packet.setAddress(SERVER_CONFIG.routeTable.get(dstVirtualAddress).physicalAddress);
            packet.setPort(SERVER_CONFIG.routeTable.get(dstVirtualAddress).clientPort);
            server.send(packet);
        }
    }

    public static void main(String[] args) throws IOException, IllegalRawDataException {
        String configPath = Resources.getResource("server_config.json").getPath();
        ForwardServer forwardServer = new ForwardServer(configPath);
        forwardServer.run();
    }
}
