package com.halakasama.client;

import com.halakasama.control.ControlChannelHandler;
import com.halakasama.control.ControlChannelMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class ClientControlChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientControlChannel.class);
    InetAddress serverAddress, clientAddress;
    int serverPort, clientPort;

    public static void main(String[] args) {
        String serverAddress = args[0];
        int serverTcpPort = Integer.parseInt(args[1]);
        int serverUdpPort = Integer.parseInt(args[2]);

        String clientAddress = args[3];
        int clientTcpPort = Integer.parseInt(args[4]);
        int clientUdpPort = Integer.parseInt(args[5]);
        int clientId = Integer.parseInt(args[6]);


        ClientContext clientContext = new ClientContext("client" + clientId,serverAddress,clientAddress,serverUdpPort,clientUdpPort);
        new ClientControlChannel(serverAddress,serverTcpPort,clientAddress,clientTcpPort).service(clientContext);
    }

    public ClientControlChannel(String serverAddress, int serverPort, String clientAddress, int clientPort) {
        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
            this.clientAddress = InetAddress.getByName(clientAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Construct failed.", e);
        }
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public void service(ClientContext clientContext) {
        Selector selector;
        SocketChannel socketChannel;

        //打开SocketChannel
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.socket().setTcpNoDelay(true);
            socketChannel.socket().setReuseAddress(true);
            socketChannel.bind(new InetSocketAddress(clientAddress,clientPort));
            socketChannel.connect(new InetSocketAddress(serverAddress, serverPort));
            socketChannel.configureBlocking(false);

            LOGGER.debug("Blocking mode {} {}", socketChannel.isBlocking(),socketChannel.isConnected());

            LOGGER.info("Client control channel server address{}:{} opened.", serverAddress.getHostAddress(), serverPort);
        } catch (IOException e) {
            LOGGER.error("Client control channel client address {}:{} server address {}:{} open failed. {}", clientAddress.getHostAddress(),clientPort,serverAddress.getHostAddress(), serverPort, e);
            return;
        }

        //开始事件驱动循环
        try {
            socketChannel.register(selector, SelectionKey.OP_READ, new ControlChannelMessageHandler(socketChannel, false, clientContext));
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    ControlChannelHandler controlChannelHandler = (ControlChannelHandler) key.attachment();
                    controlChannelHandler.handleTcpEvent(key);
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            LOGGER.error("An IOException occurred during service. Service exit.", e);
        }
    }
}