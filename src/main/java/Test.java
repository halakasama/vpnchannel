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
    }

    public static void main(String[] args) throws Exception {
//        MyMessage src = new MyMessage();
//        src.name = "msgpack";
//        src.version = 0.6;
//        src.data = new byte[]{1,2,3};
//
//        MessagePack msgpack = new MessagePack();
//        // Serialize
//        byte[] bytes = msgpack.write(src);
//        // Deserialize
//        MyMessage dst = msgpack.read(bytes, MyMessage.class);
        LinkedHashMap<String,String> a = new LinkedHashMap<>();
        a.put("abc",null);
        DatagramSocket datagramSocket = new DatagramSocket(1110, InetAddress.getByName("10.9.8.3"));
        byte[] buf = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buf,0,buf.length);
        while (true){
            datagramSocket.receive(packet);
            System.out.println(IpV4Packet.newPacket(packet.getData(),0,packet.getLength()));
        }
    }
}
