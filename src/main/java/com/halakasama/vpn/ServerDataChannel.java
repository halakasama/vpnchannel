package com.halakasama.vpn;

import com.google.common.io.Resources;
import com.halakasama.server.ServerContext;
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
public class ServerDataChannel {
    private final Logger LOGGER = LoggerFactory.getLogger(ServerDataChannel.class);

    private InetAddress serverAddress;
    private int serverUdpPort;
    private int clientUdpPort;

    public ServerDataChannel(String serverAddress, int serverUdpPort, int clientUdpPort) {
        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Construct failed.",e);
        }
        this.serverUdpPort = serverUdpPort;
        this.clientUdpPort = clientUdpPort;
    }

    public void serve(ServerContext serverContext) throws IOException, IllegalRawDataException {
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(serverUdpPort,serverAddress);
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
            if (!serverContext.isVirtualAddressValid(dstVirtualAddress)){
                LOGGER.info("VirtualAddress {} not registered yet!",dstVirtualAddress);
                continue;
            }

            //重新设置收到的udp包的目的地址和端口号，并发出
            packet.setAddress(serverContext.getPhysicalAddress(dstVirtualAddress));
            packet.setPort(clientUdpPort);
            packet.setData(msg,0,msg.length);
            server.send(packet);
        }
    }


    public static void main(String[] args) throws IOException, IllegalRawDataException {
        String configPath = Resources.getResource("server_config.json").getPath();
    }
}
