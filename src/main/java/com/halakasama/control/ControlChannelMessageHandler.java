package com.halakasama.control;

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
    private LocalContextHelper localContextHelper;

    public ControlChannelMessageHandler(SocketChannel socketChannel, boolean serverMode, LocalContextHelper localContextHelper) {
        buffer = ByteBuffer.allocate(BUF_SIZE);
        buffer.clear();

        this.localContextHelper = localContextHelper;
        this.connectContext = new ConnectContext.Builder(serverMode, socketChannel, localContextHelper).build();

        //触发协议状态机
        connectContext.getProtocolHandlerChain().trigger();
    }


    /**
     * 处理粘包，读取一个完整消息
     * @param selectionKey
     */
    @Override
    public void handleTcpEvent(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        try {
            int cnt = socketChannel.read(buffer);
            if (cnt == -1){
                LOGGER.warn("SocketChannel {}:{} reached the end of input stream!",socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort());
                localContextHelper.unregisterConnection(connectContext);
                return;
            }
        } catch (IOException e) {
            LOGGER.warn("SocketChannel {}:{} read error! {}",socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort(),e);
            localContextHelper.unregisterConnection(connectContext);
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
