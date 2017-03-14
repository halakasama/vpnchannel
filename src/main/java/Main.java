import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.halakasama.packet.DataPacket;
import com.halakasama.vpn.ForwardServer;
import com.halakasama.vpn.VPNClient;
import org.msgpack.MessagePack;
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
//        MessagePack msgPack = new MessagePack();
//        msgPack.register(DataPacket.class);
//        byte[] buf = msgPack.write(new DataPacket("192.1","192.2",new byte[]{1,2,3}));

        String mode = args[0];
        String configPath = Resources.getResource("server_config.json").getPath();
        if (mode.equals("server")){
            ForwardServer forwardServer = new ForwardServer(configPath);
            forwardServer.run();
        }else {
            int clientIndex = Integer.parseInt(args[1]);
            VPNClient vpnClient = new VPNClient(configPath,clientIndex);
            vpnClient.run();
        }
    }

}
