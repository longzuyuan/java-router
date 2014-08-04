/*
 * Created on Sep 2, 2008
 */
package code.messy.sample;

import java.net.InetAddress;

import code.messy.net.ethernet.ArpHandler;
import code.messy.net.ethernet.EthernetIpSupport;
import code.messy.net.ethernet.EthernetPort;
import code.messy.net.ethernet.Ethertype;
import code.messy.net.ip.IpMapper;
import code.messy.net.ip.IpInputPacket;
import code.messy.net.ip.NetworkNumber;
import code.messy.net.ip.dhcp.DhcpHandler;
import code.messy.net.ip.icmp.IcmpHandler;
import code.messy.net.ip.rip2.RipProcessor;
import code.messy.net.ip.route.LocalSubnet;
import code.messy.net.ip.route.RouteHandler;
import code.messy.net.ip.route.RoutingTable;
import code.messy.net.ip.udp.UdpMapper;
import code.messy.util.IpAddressHelper;

public class RipRouting {
    /**
     * Syntax: <portname> <ip> <prefix>
     * e.g: java RipRouting eth1 10.0.0.2 24 eth2 11.0.0.2 24
     * 
     * This would be pure static routing if RipProcessor is removed.
     * 
     */
    public static void main(String[] args) throws Exception {
        RouteHandler route = new RouteHandler();
        
        UdpMapper udp = new UdpMapper();

        IpMapper ipCommonMapper = new IpMapper();
        ipCommonMapper.register(IpInputPacket.Protocol.UDP, udp);
        ipCommonMapper.register(route);

        IcmpHandler icmp = new IcmpHandler();
        IpMapper ipLocalMapper = new IpMapper();
        ipLocalMapper.register(IpInputPacket.Protocol.ICMP, icmp);

        RipProcessor rip = new RipProcessor(udp);
        
        EthernetPort eths[] = new EthernetPort[2];
        
        for (int i = 0; i < 2; i++) {
        	eths[i] = new EthernetPort(args[i * 3]);
        	InetAddress ip = InetAddress.getByName(args[i * 3 + 1]);
            short prefix = Short.parseShort(args[i * 3 + 2]);
            NetworkNumber network = new NetworkNumber(ip, prefix);
            
            EthernetIpSupport ethip = new EthernetIpSupport(eths[i]);
            LocalSubnet subnet = LocalSubnet.create(network, ip, ethip, ipLocalMapper);
            
            RoutingTable.getInstance().add(subnet);
            rip.addStaticRoute(subnet);
            
            UdpMapper udpForBroadcast = new UdpMapper();
            DhcpHandler dhcp = new DhcpHandler(subnet);
            udpForBroadcast.add(IpAddressHelper.BROADCAST_ADDRESS, 67, dhcp);
            udp.add(ip, 67, dhcp);
            IpMapper ipBroadcastMapper = new IpMapper();
            ipBroadcastMapper.register(IpAddressHelper.BROADCAST_ADDRESS, IpInputPacket.Protocol.UDP, udpForBroadcast);
            ipBroadcastMapper.register(ipCommonMapper);
            
            ethip.register(ipBroadcastMapper);
            RoutingTable.getInstance().add(subnet);

            eths[i].register(Ethertype.ARP, new ArpHandler());
        }
        
        rip.start();

        for (int i = 0; i < 2; i++) {
        	eths[i].start();
        }
        
        for (int i = 0; i < 2; i++) {
        	eths[i].join();
        }
        rip.stop();
    }
}