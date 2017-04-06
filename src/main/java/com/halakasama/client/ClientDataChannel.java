package com.halakasama.client;

import com.halakasama.config.GlobalParam;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.data.DataWrapper;
import com.halakasama.packet.EtherPacketParser;
import org.p2pvpn.tuntap.TunTap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

/**
 * Created by pengfei.ren on 2017/4/4.
 */
public class ClientDataChannel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDataChannel.class);

    private TunTap tunTap;
    private EtherPacketParser etherPacketParser;
    private DatagramSocket datagramSocket;

    private InetAddress serverAddress;
    private InetAddress clientAddress;
    private int serverUdpPort;
    private int clientUdpPort;

    private ClientContext clientContext;

    private ClientDataChannel(InetAddress serverAddress, InetAddress clientAddress, int serverUdpPort, int clientUdpPort, String virtualAddress, ClientContext clientContext) {
        this.serverAddress = serverAddress;
        this.clientAddress = clientAddress;
        this.serverUdpPort = serverUdpPort;
        this.clientUdpPort = clientUdpPort;
        this.clientContext = clientContext;

        try {
            tunTap = TunTap.createTunTap();
            tunTap.setIP(virtualAddress, GlobalParam.SUBNET_MASK);
            etherPacketParser = new EtherPacketParser();
        } catch (Exception e) {
            LOGGER.error("VPNClient init failed!",e);
        }
        try {
            datagramSocket = new DatagramSocket(this.clientUdpPort,this.clientAddress);
        } catch (SocketException e) {
            LOGGER.error("Client udp socket initializing failed.",e);
        }
    }

    public void processTuntapToLink() throws IOException {
        //从tuntap虚拟网卡阻塞读取以太网帧
        byte[] inBuf = new byte[2048];
        int inLen = tunTap.read(inBuf);

        //解析以太网帧，并判断ip包是否是可处理的类型（会有操作系统发来的一系列配置报文）
        etherPacketParser.parseEtherPacket(inBuf, inLen);
//        if (!etherPacketParser.isInIp4Subnet(GlobalParam.SUBNET, GlobalParam.SUBNET_MASK)){
//            return;
//        }

        //判断是否是ip广播
        if (etherPacketParser.isIpBroadcast(GlobalParam.SUBNET_BROADCAST)){
            LOGGER.error("IP broadcast packet received from tuntap, but not supported yet!");
            return;
        }

        LOGGER.info("Subnet packet received from tuntap. \n{}", etherPacketParser.getEtherPacket());

        //如果是arp请求，则返回arp应答
        byte[] arpResponse = etherPacketParser.getArpResponse("");
        if (arpResponse!=null){
            tunTap.write(arpResponse,arpResponse.length);
            return;
        }

        //如果是一般ipv4报文，则密码学处理后发送
        byte[] message = etherPacketParser.getProcessedMessage(clientContext.getCryptoContext());
        DatagramPacket datagramPacket = new DatagramPacket(message,message.length,serverAddress,serverUdpPort);
        datagramSocket.send(datagramPacket);
    }

    public void processLinkToTuntap() throws IOException {
        //从物理网卡接收udp数据包
        byte[] buf = new byte[2048];
        DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length);
        datagramSocket.receive(datagramPacket);
        LOGGER.debug("Packet received from link. {}",datagramPacket.getData());


        //解析数据包
        DataWrapper dataWrapper = null;
        dataWrapper = DataWrapper.decode(datagramPacket.getData(),datagramPacket.getLength());
        if (dataWrapper == null){
            return;
        }
        LOGGER.debug("Data packet received from link. {}", dataWrapper);

        //检查数据包完整性
        CryptoContext cryptoContext = clientContext.getCryptoContext();
        if ( cryptoContext.checkHmacGood(dataWrapper.getHmac(),dataWrapper.getData(),dataWrapper.getKeyPtr()) ){
            LOGGER.error("Packet integrity check failed! {}", dataWrapper);
            return;
        }

        //解密数据包
        byte[] plainText = cryptoContext.decode(dataWrapper.getData(),dataWrapper.getKeyPtr());

        //写入tuntap虚拟网卡
        tunTap.write(plainText,plainText.length);
    }

    public static void startDataChannel(InetAddress serverAddress, InetAddress clientAddress, int serverUdpPort, int clientUdpPort, String virtualAddress, ClientContext clientContext){
        ClientDataChannel clientDataChannel = new ClientDataChannel(serverAddress,clientAddress,serverUdpPort,clientUdpPort,virtualAddress,clientContext);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        clientDataChannel.processTuntapToLink();
                    } catch (IOException e) {
                        LOGGER.error("ProcessTuntapToLink occurred an error!",e);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        clientDataChannel.processLinkToTuntap();
                    } catch (IOException e) {
                        LOGGER.error("ProcessLinkToTuntap occurred an error!",e);
                    }
                }
            }
        }).start();
    }
}
