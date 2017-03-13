import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.pcap4j.core.PcapNetworkInterface.*;

/**
 * Created by pengfei.ren on 2017/3/12.
 */
public class PcapMain {
    public static void main(String[] args) throws UnknownHostException, PcapNativeException, NotOpenException {
        InetAddress addr = InetAddress.getByName("10.9.8.1");
        PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
        int snapLen = 65536;
        PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
        int timeout = 10;
        PcapHandle handle = nif.openLive(snapLen, mode, timeout);
        while (true){
            Packet packet = handle.getNextPacket();
            if(packet != null) {
                System.out.println(packet);
            }
        }
    }
}
