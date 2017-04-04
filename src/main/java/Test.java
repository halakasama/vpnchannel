import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.pcap4j.packet.IpV4Packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedHashMap;

/**
 * Created by pengfei.ren on 2017/3/14.
 */
public class Test {
    @Message // Annotation
    public static class MyMessage {
        // public fields are serialized.
        public String name;
        public double version;
        public byte[] data;

        void process(){
            int i = 0;
             ++i;
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public static void start(){
            MyMessage myMessage = new MyMessage();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        myMessage.process();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception {
        MyMessage.start();
        System.out.println("a");

    }
}
