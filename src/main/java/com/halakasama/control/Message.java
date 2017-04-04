package com.halakasama.control;

import com.halakasama.config.GlobalParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created by admin on 2017/3/28.
 */
public class Message {
    private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);

    private static final int FRAME_MAX_SIZE = GlobalParam.FRAME_MAX_SIZE;
    private static final short HEADER_LEN = 6;
    private short msgLen; //消息总长度，包含长度字段本身
    public short protocolType; //协议类型
    public short msgType; // 消息类型
    public byte[] content;
    public int contentLen;//除了头部之外的内容长度

    @Override
    public String toString() {
        return "Message{" +
                "msgLen=" + msgLen +
                ", protocolType=" + protocolType +
                ", msgType=" + msgType +
                ", content=" + Arrays.toString(content) +
                ", contentLen=" + contentLen +
                '}';
    }

    public Message() {
//        content = new byte[FRAME_MAX_SIZE];
    }

    public Message(short protocolType, short msgType, byte[] content, int contentLen) {
        this.protocolType = protocolType;
        this.msgType = msgType;
        this.content = content;
        this.contentLen = contentLen;
        this.msgLen = (short)(contentLen + HEADER_LEN);
    }

    public static boolean sendMessage(SocketChannel socketChannel, Message message){
        ByteBuffer byteBuffer = Message.encode(message);//一个读模式的ByteBuffer
        if (message.content[0] == 48 ){
            System.out.println();
            LOGGER.error("{}",byteBuffer);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        while (byteBuffer.hasRemaining()) {
            LOGGER.error("{}",byteBuffer);
            try {
                socketChannel.write(byteBuffer);
            } catch (IOException e) {
                LOGGER.error("Socket channel send error! {}：{} {} {}",socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort(),message,e);
                return false;
            }
            LOGGER.error("{}",byteBuffer);
        }
        LOGGER.info("Message sent! {}",message);
        return true;
    }

    /**
     *
     * @param byteBuffer 一个读模式的ByteBuffer
     * @return
     */
    public static boolean hasCompleteMessage(ByteBuffer byteBuffer){
        if (byteBuffer.remaining() > 2){
            short msgLen = byteBuffer.getShort();
            byteBuffer.rewind();
            if (byteBuffer.remaining() >= msgLen){
                return true;
            }
        }
        LOGGER.error("Incomplete message received! {}",byteBuffer);
        return false;
    }

    /**
     * 将读到的ByteBuffer
     * @param byteBuffer 一个读模式的ByteBuffer
     * @return
     */
    public static Message decode(ByteBuffer byteBuffer){
        Message msg = new Message();
        msg.msgLen = byteBuffer.getShort();
        msg.contentLen = (short) (msg.msgLen - HEADER_LEN);
        msg.protocolType = byteBuffer.getShort();
        msg.msgType = byteBuffer.getShort();
        msg.content = new byte[msg.contentLen];
        byteBuffer.get(msg.content);
        LOGGER.info("Message received. {}",msg);
        return msg;
    }

    /**
     *
     * @param message
     * @return 返回根据Message消息组装好的读模式的ByteBuffer
     */
    public static ByteBuffer encode(Message message){
        ByteBuffer byteBuffer = ByteBuffer.allocate(FRAME_MAX_SIZE);
        byteBuffer.clear();
        byteBuffer.putShort(message.msgLen)
                .putShort(message.protocolType)
                .putShort(message.msgType)
                .put(message.content,0,message.contentLen);
        byteBuffer.flip();
        return byteBuffer;
    }
}
