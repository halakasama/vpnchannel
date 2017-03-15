package com.halakasama.packet;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.google.common.base.Strings;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.Inet4NetworkAddress;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by pengfei.ren on 2017/3/13.
 */
public class EtherPacketParser {
    private EthernetPacket.EthernetHeader ethernetHeader; //如果是以太网帧，则非空
    private IpV4Packet.IpV4Header ipV4Header;//如果是ipv4包，则非空
    private ArpPacket.ArpHeader arpRequestHeader;//如果是arp请求，则非空
    private EthernetPacket ethernetPacket;
    private byte[] buffer;
    private int length;

    private final Logger LOGGER = LoggerFactory.getLogger(EtherPacketParser.class);

    public void parseEtherPacket(byte[] buffer, int length){
        initMembers();
        this.buffer = buffer;
        this.length = length;
        try {
            ethernetPacket = EthernetPacket.newPacket(buffer,0,length);
            ethernetHeader = ethernetPacket.getHeader();
            if (ethernetHeader.getType().equals(EtherType.ARP)){
                arpRequestHeader = ((ArpPacket)ethernetPacket.getPayload()).getHeader();
                if (!arpRequestHeader.getOperation().equals(ArpOperation.REQUEST)){
                    arpRequestHeader = null;
                }
            }else if (ethernetHeader.getType().equals(EtherType.IPV4)){
                ipV4Header = ((IpV4Packet)ethernetPacket.getPayload()).getHeader();
            }else {
                LOGGER.info("This is an ethernet packet, but neither an ARP nor an IPv4. " +
                        "The 3rd layer packet type is {}.", ethernetHeader.getType());
            }
        } catch (IllegalRawDataException e) {
            initMembers();
            LOGGER.info("Ethernet packet parsing failed! This is not an ethernet packet!",e);
        }
    }

    private void initMembers(){
        ethernetHeader = null;
        ipV4Header = null;
        arpRequestHeader = null;
        ethernetPacket = null;
        buffer = null;
        length = 0;
    }


    public byte[] checkSubnetAndGetArpResponse(String ip4Address, String mask, String macAddress){
        if (isInIp4Subnet(ip4Address,mask)){
            return getArpResponse(macAddress);
        }
        return null;
    }

    public byte[] getArpResponse(String macAddress){
        if (!isArpRequest()) {
            return null;
        }

        if (StringUtils.isEmpty(macAddress)){
            macAddress = "ff:ff:ff:ff:ff:ff";
        }
        ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
        arpBuilder.hardwareType(ArpHardwareType.ETHERNET)
                .protocolType(EtherType.IPV4)
                .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES)
                .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                .operation(ArpOperation.REPLY)
                .srcHardwareAddr(MacAddress.getByName(macAddress))
                .srcProtocolAddr(arpRequestHeader.getDstProtocolAddr())
                .dstHardwareAddr(arpRequestHeader.getSrcHardwareAddr())
                .dstProtocolAddr(arpRequestHeader.getSrcProtocolAddr());

        EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
        etherBuilder.dstAddr(ethernetHeader.getSrcAddr())
                .srcAddr(MacAddress.getByName(macAddress))
                .type(EtherType.ARP)
                .payloadBuilder(arpBuilder)
                .paddingAtBuild(true);

        return etherBuilder.build().getRawData();
    }


    public boolean isEtherPacket(){
        return ethernetHeader!=null;
    }

    public boolean isArpRequest(){
        return arpRequestHeader!=null;
    }

    public boolean isIpV4Packet(){
        return ipV4Header!=null;
    }

    public Packet getEtherPacket(){
        return ethernetPacket;
    }
    /**
     * 判断ARP或IPv4包的源和目的网络地址是否均位于指定子网
     * @param ip4Address
     * @param mask
     * @return
     */
    public boolean isInIp4Subnet(String ip4Address, String mask){
        if (!isArpRequest() && !isIpV4Packet()){
            return false;
        }
        try {
            byte[] ip4AddressByte = InetAddress.getByName(ip4Address).getAddress();
            byte[] maskByte = InetAddress.getByName(mask).getAddress();
            byte[] srcAddrByte = isIpV4Packet()?ipV4Header.getSrcAddr().getAddress():arpRequestHeader.getSrcProtocolAddr().getAddress();
            byte[] dstAddrByte = isIpV4Packet()?ipV4Header.getDstAddr().getAddress():arpRequestHeader.getDstProtocolAddr().getAddress();
            for (int i = 0; i < srcAddrByte.length; ++i){
                srcAddrByte[i] = (byte) (srcAddrByte[i]&maskByte[i]);
                dstAddrByte[i] = (byte) (dstAddrByte[i]&maskByte[i]);
                ip4AddressByte[i] = (byte) (ip4AddressByte[i]&maskByte[i]);
            }
            return (Arrays.equals(ip4AddressByte,srcAddrByte))&&(Arrays.equals(ip4AddressByte,dstAddrByte));
        } catch (Exception e) {
            LOGGER.error("Function isInIp4Subnet invalid input : ip4Address {},\tmask {}.",ip4Address,mask );
            return false;
        }
    }

    public DataPacket getDataPacket(){
        if (!isIpV4Packet()){
            return null;
        }
        return new DataPacket(ipV4Header.getSrcAddr().getHostAddress(),ipV4Header.getDstAddr().getHostAddress(),Arrays.copyOf(buffer,length));
    }

    public boolean isIpBroadcast(String broadcastAddress){
        return StringUtils.equals(broadcastAddress,ipV4Header.getDstAddr().getHostAddress());
    }
}
