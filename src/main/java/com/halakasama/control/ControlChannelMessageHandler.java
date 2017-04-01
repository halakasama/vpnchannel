package com.halakasama.control;

import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolHandler;
import com.halakasama.control.server.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by admin on 2017/3/27.
 */
public class ControlChannelMessageHandler implements ControlChannelHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlChannelMessageHandler.class);
    private static final int BUF_SIZE = 1024;
    private ByteBuffer buffer;

    private ConnectContext connectContext;

    public ControlChannelMessageHandler(SocketChannel socketChannel, boolean serverMode, LocalContextHelper localContextHelper) {
        buffer = ByteBuffer.allocate(BUF_SIZE);
        buffer.clear();

        this.connectContext = new ConnectContext.Builder(serverMode, socketChannel, localContextHelper).build();
    }


    /**
     * 处理粘包，读取一个完整消息
     * @param selectionKey
     */
    @Override
    public void handleTcpEvent(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        try {
            socketChannel.read(buffer);
        } catch (IOException e) {
            LOGGER.error("SocketChannel {}:{} read error! {}",socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort(),e);
            return;
        }

        buffer.flip();
        if (Message.hasCompleteMessage(buffer)){
            Message message = Message.decode(buffer);
            connectContext.getProtocolHandlerChain().handle(message);
        }
        buffer.compact();
    }
}
