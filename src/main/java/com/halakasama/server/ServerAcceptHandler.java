package com.halakasama.server;

import com.halakasama.control.ControlChannelHandler;
import com.halakasama.control.ControlChannelMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by admin on 2017/3/27.
 */
public class ServerAcceptHandler implements ControlChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAcceptHandler.class);
    private ServerContext serverContext;

    public ServerAcceptHandler(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public void handleTcpEvent(SelectionKey selectionKey) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
        Selector selector = selectionKey.selector();
        SocketChannel socketChannel;
        try {
            socketChannel = serverSocketChannel.accept();
            socketChannel.socket().setReuseAddress(true);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ, new ControlChannelMessageHandler(socketChannel,true, serverContext));

        }catch (IOException e){
            LOGGER.error("Client connection accept failed.",e);
            return;
        }

        LOGGER.info("Client {}:{} connection received.",socketChannel.socket().getInetAddress(),socketChannel.socket().getPort());
    }
}
