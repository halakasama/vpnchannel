package com.halakasama.control.server;

import com.halakasama.config.ServerConfiguration;
import com.halakasama.control.ControlChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
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
    Selector selector;
    ServerSocketChannel serverSocketChannel;

    public void service(ServerConfiguration serverConfig){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(serverConfig.serverAddress, serverConfig.serverPort))
                    .configureBlocking(false);
            selector = Selector.open();
            LOGGER.info("Server control channel {}:{} opened.", serverConfig.serverAddress.getHostAddress(), serverConfig.serverPort);
        }catch (IOException e){
            LOGGER.error("Server control channel {}:{} open failed. {}",serverConfig.serverAddress.getHostAddress(), serverConfig.serverPort,e);
            return;
        }

        try {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new ServerAcceptHandler(null));
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
