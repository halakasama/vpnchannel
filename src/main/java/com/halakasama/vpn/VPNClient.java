package com.halakasama.vpn;

import com.google.common.io.Resources;
import com.halakasama.config.ClientConfiguration;
import com.halakasama.config.Configuration;
import com.halakasama.packet.DataPacket;
import com.halakasama.packet.EtherPacketParser;
import org.msgpack.MessagePack;
import org.p2pvpn.tuntap.TunTap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


/**
 * Created by pengfei.ren on 2017/3/13.
 */
public class VPNClient {
    private final Logger LOGGER = LoggerFactory.getLogger(VPNClient.class);

    private TunTap tunTap;
    private EtherPacketParser etherPacketParser;
    private final ClientConfiguration CLIENT_CONFIG;
    private DatagramSocket datagramSocket;

    public VPNClient(String configPath, int clientIndex) {
        CLIENT_CONFIG = (ClientConfiguration) Configuration.getConfiguration(configPath,true,clientIndex);
        try {
            tunTap = TunTap.createTunTap();
            tunTap.setIP(CLIENT_CONFIG.clientSocket.virtualAddress, CLIENT_CONFIG.virtualMask);
            etherPacketParser = new EtherPacketParser();
        } catch (Exception e) {
            LOGGER.error("VPNClient init failed!",e);
        }
        try {
            datagramSocket = new DatagramSocket(CLIENT_CONFIG.clientPort,CLIENT_CONFIG.clientSocket.physicalAddress);
        } catch (SocketException e) {
            LOGGER.error("Client udp socket initializing failed.",e);
        }
    }

    public void processTuntapToLink() throws IOException {
        byte[] inBuf = new byte[2048];
        int inLen = tunTap.read(inBuf);
        etherPacketParser.parseEtherPacket(inBuf, inLen);
        if (!etherPacketParser.isInIp4Subnet(CLIENT_CONFIG.virtualNetwork,CLIENT_CONFIG.virtualMask)){
            return;
        }

        if (etherPacketParser.isIpBroadcast(CLIENT_CONFIG.virtualBroadcast)){
            LOGGER.error("IP broadcast packet received from tuntap, but not supported yet!");
            return;
        }

        LOGGER.info("Subnet packet received from tuntap. \n{}", etherPacketParser.getEtherPacket());

        byte[] arpResponse = etherPacketParser.getArpResponse("");
        if (arpResponse!=null){
            tunTap.write(arpResponse,arpResponse.length);
            return;
        }

        MessagePack msgPack = new MessagePack();
        DataPacket dataPacket = etherPacketParser.getDataPacket();
        byte[] data = msgPack.write(dataPacket);
        DatagramPacket datagramPacket = new DatagramPacket(data,data.length,CLIENT_CONFIG.serverAddress,CLIENT_CONFIG.serverPort);
        datagramSocket.send(datagramPacket);
    }
    public void processLinkToTuntap() throws IOException {
        byte[] buf = new byte[2048];
        DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length);
        datagramSocket.receive(datagramPacket);
        MessagePack msgPack = new MessagePack();
        DataPacket dataPacket = msgPack.read(datagramPacket.getData(),DataPacket.class);
        tunTap.write(dataPacket.data,dataPacket.data.length);
    }

    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        processTuntapToLink();
                    } catch (IOException e) {
                        LOGGER.error("ProcessTuntapToLink occurred an error!",e);
                    }
                }
            }
        }).start();

        while (true){
            try {
                processLinkToTuntap();
            } catch (IOException e) {
                LOGGER.error("ProcessLinkToTuntap occurred an error!",e);
            }
        }
    }

    public static void main(String[] args) {
        String configPath = Resources.getResource("server_config.json").getPath();
        VPNClient vpnClient = new VPNClient(configPath,0);
        vpnClient.run();
    }
}
