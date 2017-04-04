package com.halakasama.server;

import com.halakasama.control.ControlChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by admin on 2017/3/27.
 */
public class ServerControlChannel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerControlChannel.class);
    InetAddress serverAddress;
    int serverTcpPort;

    public static void main(String[] args) {
        String serverAddress = args[0];
        int serverTcpPort = Integer.parseInt(args[1]);
        int serverUdpPort = Integer.parseInt(args[2]);
        int clientUdpPort = Integer.parseInt(args[3]);

        ServerContext serverContext = new ServerContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ServerDataChannel(serverAddress,serverUdpPort,clientUdpPort).serve(serverContext);
            }
        }).start();
        new ServerControlChannel(serverAddress,serverTcpPort).service(serverContext);
    }

    public ServerControlChannel(String serverAddress, int serverTcpPort) {
        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Construct failed.",e);
        }
        this.serverTcpPort = serverTcpPort;
    }

    public void service(ServerContext serverContext){
        Selector selector;
        ServerSocketChannel serverSocketChannel;

        //打开ServerSocketChannel
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(serverAddress, serverTcpPort))
                    .configureBlocking(false);
            LOGGER.error("Blocking mode {}", serverSocketChannel.isBlocking());
            LOGGER.info("Server control channel {}:{} opened.", serverAddress.getHostAddress(), serverTcpPort);
        }catch (IOException e){
            LOGGER.error("Server control channel {}:{} open failed. {}",serverAddress.getHostAddress(), serverTcpPort,e);
            return;
        }

        //开始事件驱动循环
        try {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new ServerAcceptHandler(serverContext));
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
        }catch (IOException e){
            LOGGER.error("An IOException occurred during service. Service exit.",e);
        }
    }
}
