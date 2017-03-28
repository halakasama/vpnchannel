package com.halakasama.control;

import com.halakasama.control.protocal.Message;
import com.halakasama.control.protocal.ProtocolHandler;
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

    ConnectContext connectContext;
    ProtocolHandler protocolHandlerChain;

    public ControlChannelMessageHandler() {
        buffer = ByteBuffer.allocate(BUF_SIZE);
        buffer.clear();
    }

    public void initProtocolHandlerChain(){

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
            protocolHandlerChain.handle(message);
        }
        buffer.compact();
    }
}
