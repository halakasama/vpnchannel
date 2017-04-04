package com.halakasama.data;

import com.google.common.primitives.Bytes;
import com.halakasama.control.crypto.CryptoContext;
import com.halakasama.keymanage.CipherPair;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class DataWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataWrapper.class);
    private static final int INET_ADDR_LEN = 4;
    private static final int KEY_PTR_LEN = 4;
    private static final int HEADER_LEN = 2 + INET_ADDR_LEN *2 + KEY_PTR_LEN + 2;
    private short length;
    private String srcAddress;
    private String dstAddress;
    private int keyPtr;
    private short dataLen;
    private byte[] data;
    private byte[] hmac;

    private DataWrapper(){}
    private DataWrapper(Builder builder){
        this.length = builder.length;
        this.srcAddress = builder.srcAddress;
        this.dstAddress = builder.dstAddress;
        this.keyPtr = builder.keyPtr;
        this.dataLen = builder.dataLen;
        this.data = builder.data;
        this.hmac = builder.hmac;
    }

    public static class Builder {
        private CryptoContext cryptoContext;

        private short length;
        private String srcAddress;
        private String dstAddress;
        private int keyPtr;
        private short dataLen;
        private byte[] data;
        private byte[] hmac;

        private byte[] preHmac;

        public Builder(CryptoContext cryptoContext){
            this.cryptoContext = cryptoContext;
        }

        public Builder srcAddress(String srcAddress) {
            this.srcAddress = srcAddress;
            return this;
        }

        public Builder dstAddress(String dstAddress) {
            this.dstAddress = dstAddress;
            return this;
        }

        public Builder keyPtr(int keyPtr) {
            this.keyPtr = keyPtr;
            return this;
        }

        public Builder data(byte[] data, short dataLen) {
            this.data = data;
            this.dataLen = dataLen;
            if (data.length > dataLen){
                this.data = Arrays.copyOf(data,dataLen);
            }
            return this;
        }

        public Builder prepareHmac(){
            length = (short) (HEADER_LEN + this.dataLen + cryptoContext.getHmacLength());
            int contentLen = HEADER_LEN + this.dataLen;
            ByteBuffer byteBuffer = ByteBuffer.allocate(contentLen);
            byteBuffer.clear();
            byteBuffer.putShort(length);
            byteBuffer.put(InetUtil.inetFromStringToByte(srcAddress));
            byteBuffer.put(InetUtil.inetFromStringToByte(dstAddress));
            byteBuffer.putInt(keyPtr);
            byteBuffer.putShort(dataLen);
            byteBuffer.put(data);

            preHmac = byteBuffer.array();
            CipherPair cipherPair = cryptoContext.getHmac(preHmac);
            hmac = cipherPair.cipherText;
            return this;
        }

        public byte[] getMessage(){
            return Bytes.concat(preHmac,hmac);
        }

        public DataWrapper getDataWrapper(){
            return new DataWrapper(this);
        }
    }

    public short getLength() {
        return length;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public int getKeyPtr() {
        return keyPtr;
    }

    public short getDataLen() {
        return dataLen;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getHmac() {
        return hmac;
    }



    @Override
    public String toString() {
        return "DataWrapper{" +
                "length=" + length +
                ", srcAddress='" + srcAddress + '\'' +
                ", dstAddress='" + dstAddress + '\'' +
                ", keyPtr=" + keyPtr +
                ", dataLen=" + dataLen +
                ", data=" + Arrays.toString(data) +
                ", hmac=" + Arrays.toString(hmac) +
                '}';
    }

    /**

     * 将DataWrapper对象序列化为字节消息
     * @param dataWrapper
     * @return
     */
    public static byte[] encode(DataWrapper dataWrapper){
        if (dataWrapper.dataLen != dataWrapper.data.length){
            LOGGER.debug("Data field and length data not match. {}", dataWrapper);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_LEN + dataWrapper.dataLen + dataWrapper.hmac.length);
        byteBuffer.clear();
        byteBuffer.putShort(dataWrapper.length);
        byteBuffer.put(InetUtil.inetFromStringToByte(dataWrapper.srcAddress));
        byteBuffer.put(InetUtil.inetFromStringToByte(dataWrapper.dstAddress));
        byteBuffer.putInt(dataWrapper.keyPtr);
        byteBuffer.putShort(dataWrapper.dataLen);
        byteBuffer.put(dataWrapper.data);
        byteBuffer.put(dataWrapper.hmac);
        return byteBuffer.array();
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
        System.out.println(ByteUtils.toHexString(inetAddress.getAddress()));
        System.out.println(ByteUtils.toBinaryString(inetAddress.getAddress()));

    }

    /**
     * 将消息反序列化为DataWrapper类
     * @param message
     * @return
     */
    public static DataWrapper decode(byte[] message, int length) {
        DataWrapper dataWrapper = new DataWrapper();
        ByteBuffer byteBuffer = ByteBuffer.wrap(message,0,length);

        dataWrapper.length = byteBuffer.getShort();
        if (dataWrapper.length != length){
            LOGGER.error("Message incomplete! Actual length {}, expecting {}.",length, dataWrapper.length);
            return null;
        }

        byte[] addr = new byte[INET_ADDR_LEN];
        byteBuffer.get(addr,0,INET_ADDR_LEN);
        dataWrapper.srcAddress = InetUtil.inetFromByteToString(addr);
        byteBuffer.get(addr,0,INET_ADDR_LEN);
        dataWrapper.dstAddress = InetUtil.inetFromByteToString(addr);

        dataWrapper.keyPtr = byteBuffer.getInt();
        dataWrapper.dataLen = byteBuffer.getShort();

        byte[] data = new byte[dataWrapper.dataLen];
        byteBuffer.get(data,0,dataWrapper.dataLen);
        dataWrapper.data = data;

        int hmacLen = dataWrapper.length - HEADER_LEN - dataWrapper.dataLen;
        byte[] hmac = new byte[hmacLen];
        byteBuffer.get(hmac,0,hmacLen);
        dataWrapper.hmac = hmac;

        return dataWrapper;
    }
}
