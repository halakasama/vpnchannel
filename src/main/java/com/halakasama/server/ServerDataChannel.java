package com.halakasama.server;

import com.google.common.io.Resources;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.data.DataWrapper;
import com.halakasama.keymanage.CipherPair;
import org.apache.commons.lang3.StringUtils;
import org.pcap4j.packet.IllegalRawDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

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

    public void processAndSend(DatagramPacket datagramPacket, DatagramSocket datagramSocket, ServerContext serverContext){
        //解析数据报
        DataWrapper dataWrapper = null;
        dataWrapper = DataWrapper.decode(datagramPacket.getData(),datagramPacket.getLength());
        if (dataWrapper == null){
            return;
        }
        LOGGER.debug("Data packet received. {}", dataWrapper);

        //检查数据包源物理地址和虚拟地址是否匹配
        if (!StringUtils.equals(datagramPacket.getAddress().getHostAddress(),serverContext.getPhysicalAddress(dataWrapper.getSrcAddress()).getHostAddress())){
            LOGGER.error("Physical address and virtual address do not match. {}",dataWrapper);
            return;
        }

        //检查数据包完整性
        CryptoContext cryptoContext = serverContext.getCryptoContext(dataWrapper.getSrcAddress());
        if (cryptoContext == null){
            return;
        }
        if ( cryptoContext.checkHmacGood(dataWrapper.getHmac(),dataWrapper.getData(),dataWrapper.getKeyPtr()) ){
            LOGGER.error("Packet integrity check failed! {}", dataWrapper);
            return;
        }

        //解密数据包
        byte[] plainText = cryptoContext.decode(dataWrapper.getData(),dataWrapper.getKeyPtr());

        //加密数据
        cryptoContext = serverContext.getCryptoContext(dataWrapper.getDstAddress());
        if (cryptoContext == null){
            return;
        }
        CipherPair cipherPair = cryptoContext.encode(plainText);

        //计算hmac 并获得序列化消息
        byte[] message = new DataWrapper.Builder(cryptoContext)
                .srcAddress(dataWrapper.getSrcAddress())
                .dstAddress(dataWrapper.getDstAddress())
                .keyPtr(cipherPair.keyPtr)
                .data(cipherPair.cipherText,(short) cipherPair.size)
                .prepareHmac()
                .getMessage();

        //发送数据包
        datagramPacket.setAddress(serverContext.getPhysicalAddress(dataWrapper.getDstAddress()));
        datagramPacket.setPort(clientUdpPort);
        datagramPacket.setData(message,0,message.length);
        try {
            datagramSocket.send(datagramPacket);
            LOGGER.info("转发数据包发送成功。 {}",DataWrapper.decode(message,message.length));
        } catch (IOException e) {
            LOGGER.error("数据包发送失败 {}.",DataWrapper.decode(message,message.length));
        }
    }

    public void serve(ServerContext serverContext) {
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(serverUdpPort,serverAddress);
        } catch (SocketException e) {
            LOGGER.error("Server socket build failed.",e);
        }

        while (null!=server){
            //阻塞接收udp消息
            byte[] buf = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                server.receive(packet);
            } catch (IOException e) {
                LOGGER.error("Server data channel receive error. {}",e);
                continue;
            }

            //处理并发送
            processAndSend(packet, server, serverContext);
        }
    }


    public static void main(String[] args) throws IOException, IllegalRawDataException {
        String configPath = Resources.getResource("server_config.json").getPath();
    }
}
