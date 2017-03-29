package com.halakasama.control.server;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.ControlChannelHandler;
import com.halakasama.control.ControlChannelMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * Created by admin on 2017/3/27.
 */
public class ServerAcceptHandler implements ControlChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAcceptHandler.class);
    private Map<String, ConnectContext> clientContext;
//    = new ConcurrentHashMap<>();

    public ServerAcceptHandler(Map<String, ConnectContext> clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void handleTcpEvent(SelectionKey selectionKey) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
        Selector selector = selectionKey.selector();
        SocketChannel socketChannel;
        try {
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ, new ControlChannelMessageHandler());
            //todo 服务器发起对客户端的认证
//            clientContext.put(socketChannel.socket().getInetAddress().getHostAddress(),new ConnectContext());
        }catch (IOException e){
            LOGGER.error("Client connection accept failed.",e);
            return;
        }

        LOGGER.info("Client {}:{} connection received.",socketChannel.socket().getInetAddress(),socketChannel.socket().getPort());
    }
}
