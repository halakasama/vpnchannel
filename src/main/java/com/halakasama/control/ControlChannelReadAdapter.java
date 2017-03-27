package com.halakasama.control;

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
public class ControlChannelReadAdapter implements ControlChannelHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlChannelReadAdapter.class);
    private static final int BUF_SIZE = 1024;
    private static final int FRAME_MAX_SIZE = 1024;
    ByteBuffer buffer;
    short frameLen;
    byte[] frameContent;

    ConnectContext connectContext;
    ProtocolHandler protocolHandler;

    public ControlChannelReadAdapter() {
        buffer = ByteBuffer.allocate(BUF_SIZE);
        buffer.clear();
        frameLen = 0;
        frameContent = new byte[FRAME_MAX_SIZE];
    }

    @Override
    public void handleTcpEvent(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        try {
            socketChannel.read(buffer);
        } catch (IOException e) {
            LOGGER.error("SocketChannel {}:{} read error! {}",socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort(),e);
        }

        buffer.flip();
        if (frameLen == 0 && buffer.remaining() > 2){
            frameLen = buffer.getShort();
            buffer.rewind();
        }
        if (frameLen != 0 && buffer.remaining() >= frameLen){
            buffer.get(frameContent,0,frameLen);
            frameLen = 0;
        }
        buffer.compact();
    }
}
