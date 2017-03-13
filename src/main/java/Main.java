import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.p2pvpn.tuntap.TunTap;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by pengfei.ren on 2017/3/12.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("hello world");
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("os.name"));
        InetAddress inetAddress = InetAddress.getByName(null);
        final byte[] bytes = inetAddress.getAddress();
        inetAddress = InetAddress.getByName("192.168.0.1");
        System.out.println(InetAddress.getByName("192.168.0.1"));

        System.out.println("ab\nac");
        String resourcePath = Resources.getResource("").getPath();
        System.out.println(resourcePath);

/*        TunTap tunTap = null;
        try {
            tunTap = TunTap.createTunTap();
            tunTap.setIP("10.9.8.10","255.255.255.0");
            System.out.println(tunTap.getDev());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        new Thread(new Runnable() {
            public void run() {
                byte[] buffer = new byte[2048];
                TunTap tunTap = null;
                try {
                    tunTap = TunTap.createTunTap();
                    tunTap.setIP("10.9.8.11","255.255.255.0");
                    System.out.println(tunTap.getDev());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (tunTap != null){
                    try {
                        int length = tunTap.read(buffer);
                        EthernetPacket packet = EthernetPacket.newPacket(buffer,0,length);
                        if (packet.getHeader().getType().equals(EtherType.ARP)){
                            ArpPacket.ArpHeader arpHeader = ((ArpPacket)packet.getPayload()).getHeader();

                            if (arpHeader.getOperation().equals(ArpOperation.REQUEST)
                                    && arpHeader.getDstProtocolAddr().getHostAddress().startsWith("10.9.8")){
                                String addr = arpHeader.getDstProtocolAddr().getHostAddress();
                                ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
                                arpBuilder.hardwareType(ArpHardwareType.ETHERNET)
                                        .protocolType(EtherType.IPV4)
                                        .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES)
                                        .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                                        .operation(ArpOperation.REPLY)
                                        .srcHardwareAddr(MacAddress.getByName("ff:ff:ff:ff:ff:ff"))
                                        .srcProtocolAddr(arpHeader.getDstProtocolAddr())
                                        .dstHardwareAddr(arpHeader.getSrcHardwareAddr())
                                        .dstProtocolAddr(arpHeader.getSrcProtocolAddr());

                                EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
                                etherBuilder.dstAddr(packet.getHeader().getSrcAddr())
                                        .srcAddr(MacAddress.getByName("ff:ff:ff:ff:ff:ff"))
                                        .type(EtherType.ARP)
                                        .payloadBuilder(arpBuilder)
                                        .paddingAtBuild(true);
                                byte[] buf = etherBuilder.build().getRawData();
                                tunTap.write(buf,buf.length);
                            }
                        }

                        System.out.println(packet);
/*                        if (packet.getHeader().getType().equals(EtherType.IPV4)){
                            IpV4Packet packetV4 = (IpV4Packet) packet.getPayload();
                            System.out.println(packetV4);
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        Thread.sleep(2000);
        DatagramSocket client = new DatagramSocket();
        byte[] data = new byte[]{0,1,2,3};
        int t = 4;
        while (t--!=0) {
            client.send(new DatagramPacket(data, data.length,InetAddress.getByName("10.9.8.4"),1234));
        }

    }
}
