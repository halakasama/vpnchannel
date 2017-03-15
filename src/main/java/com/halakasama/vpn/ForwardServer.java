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
import java.util.Arrays;

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

        while (null!=server){
            //接收udp消息
            byte[] buf = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            MessagePack msgPack = new MessagePack();
            server.receive(packet);

            //解析byte[]消息为DataPacket对象
            byte[] msg = Arrays.copyOf(packet.getData(),packet.getLength());
            LOGGER.info("Msg received length {}, msg {}",packet.getLength(),msg);
            DataPacket dataPacket = null;
            try {
                dataPacket = msgPack.read(msg,DataPacket.class);
                LOGGER.info("Ethernet frame {}", EthernetPacket.newPacket(dataPacket.data,0,dataPacket.data.length));
            } catch (IOException e) {
                LOGGER.error("MessagePack read error!",e);
                continue;
            }

            //根据DataPacket的目的虚拟地址获得目的真实地址
            String dstVirtualAddress = dataPacket.dstAddress;
            if (!SERVER_CONFIG.routeTable.containsKey(dstVirtualAddress)){
                LOGGER.info("VirtualAddress {} not registered yet!",dstVirtualAddress);
                continue;
            }

            //重新设置收到的udp包的目的地址和端口号，并发出
            packet.setAddress(SERVER_CONFIG.routeTable.get(dstVirtualAddress).physicalAddress);
            packet.setPort(SERVER_CONFIG.routeTable.get(dstVirtualAddress).clientPort);
            packet.setData(msg,0,msg.length);
            server.send(packet);
//            server.send(new DatagramPacket(new byte[]{1,2,3},0,3,SERVER_CONFIG.routeTable.get(dstVirtualAddress).physicalAddress,SERVER_CONFIG.routeTable.get(dstVirtualAddress).clientPort));
        }
    }

    public static void main(String[] args) throws IOException, IllegalRawDataException {
        String configPath = Resources.getResource("server_config.json").getPath();
        ForwardServer forwardServer = new ForwardServer(configPath);
        forwardServer.run();
    }
}
