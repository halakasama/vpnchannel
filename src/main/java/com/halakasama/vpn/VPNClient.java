package com.halakasama.vpn;

import com.halakasama.packet.EtherPacketParser;
import org.p2pvpn.tuntap.TunTap;
import org.p2pvpn.tuntap.TunTapWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pengfei.ren on 2017/3/13.
 */
public class VPNClient {
    private TunTap tunTap;
    private EtherPacketParser etherPacketParser;
    private String subnet;
    private String mask;
    private String ipAddress;

    private final Logger LOGGER = LoggerFactory.getLogger(VPNClient.class);

    public VPNClient(String subnet, String mask, String ipAddress) {
        this.subnet = subnet;
        this.mask = mask;
        this.ipAddress = ipAddress;
        try {
            tunTap = TunTap.createTunTap();
            tunTap.setIP(ipAddress, mask);
            etherPacketParser = new EtherPacketParser();
        } catch (Exception e) {
            LOGGER.error("VPNClient init failed!",e);
        }
    }

    public void processTuntapToLink(){
        byte[] inBuf = new byte[2048];
        int inLen = tunTap.read(inBuf);
        etherPacketParser.parseEtherPacket(inBuf, inLen);
        if (!etherPacketParser.isInIp4Subnet(subnet,mask)){
            return;
        }
        LOGGER.info("Subnet packet received. \n{}", etherPacketParser.getEtherPacket());

        byte[] arpResponse = etherPacketParser.getArpResponse("");
        if (arpResponse!=null){
            tunTap.write(arpResponse,arpResponse.length);
        }

    }
    public void processLinkToTuntap(){

    }

    public static void main(String[] args) {
        VPNClient vpnClient = new VPNClient("10.9.8.0","255.255.255.0","10.9.8.10");
        while (true){
            vpnClient.processTuntapToLink();
        }
    }
}
