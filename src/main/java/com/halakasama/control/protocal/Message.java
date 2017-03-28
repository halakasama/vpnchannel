package com.halakasama.control.protocal;

import com.halakasama.config.GlobalParam;

import java.nio.ByteBuffer;

/**
 * Created by admin on 2017/3/28.
 */
public class Message {
    private static final int FRAME_MAX_SIZE = GlobalParam.FRAME_MAX_SIZE;
    private static final short HEADER_LEN = 6;
    private short msgLen; //消息总长度，包含长度字段本身
    public short protocolType; //协议类型
    public short msgType; // 消息类型
    private byte[] content = new byte[FRAME_MAX_SIZE];
    private short contentLen;//除了头部之外的内容长度

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
        return false;
    }

    /**
     *
     * @param byteBuffer 一个读模式的ByteBuffer
     * @return
     */
    public static Message decode(ByteBuffer byteBuffer){
        Message msg = new Message();
        msg.msgLen = byteBuffer.getShort();
        msg.contentLen = (short) (msg.msgLen - HEADER_LEN);
        msg.protocolType = byteBuffer.getShort();
        msg.msgType = byteBuffer.getShort();
        byteBuffer.get(msg.content,0,msg.contentLen);
        return msg;
    }
}
