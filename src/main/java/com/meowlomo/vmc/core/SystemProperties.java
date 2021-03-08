package com.meowlomo.vmc.core;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.Constant;
import com.meowlomo.vmc.model.Worker;
import com.meowlomo.vmc.util.PlatformUtil;
import com.meowlomo.vmc.util.SQLiteConnect;

@Component
public class SystemProperties {	
	//TODO
	public static boolean bInited = false;
	
	@Value("${meowlomo.config.vmc.vpncontext}")
	private boolean bUseHardCodedIP;
	
	public static String architecture;
	public static int cores;
	public static String hostname;
	
	@Value("${meowlomo.config.vmc.hardcodeipaddress}")
	private String hardCodedIPAddress;
	
	@Value("${meowlomo.config.vmc.hardcodemac}")
	private String hardCodedMacAddress;
	
	public static String ipAddress;
	public static String name;
	
	@Value("${server.port}")
	public int port;
	
	public static long ram;
	public static String systemName;
	public static String systemVersion;
	
	@Value("${meowlomo.config.vmc.group}")
	private String vmcGroup;
	
	public static String macAddress;
	
	@Value("${meowlomo.config.vmc.bandwidth}")
	private int bandwidth;

	@Value("${meowlomo.config.vmc.systemMemory}")
	private int vmcSystemMem;
	
	@Autowired
	ServerCommunicator serverCommunicator;
	
	//return valuables as json
	public Worker getSystemPropertiesForRegistration(){
		
		initSystemProperties();
		
		Worker worker = new Worker();
		worker.setArchitecture(architecture);
		worker.setCpuCore(cores);
		//TODO
		worker.setBandwidth(100);	//bandwidth
		worker.setHostname(hostname);
		worker.setIpAddress(ipAddress);
		worker.setName(name);
		worker.setGroup(vmcGroup);
		worker.setStatus(Constant.STATUS_FREE);//"HOLD"
		worker.setMacAddress(macAddress);
		worker.setOperatingSystem(ComputerIdentifier.getOperatingSystemName());
		worker.setPort(port);
		worker.setRam((vmcSystemMem > 0 && vmcSystemMem < 10240) ? vmcSystemMem : ComputerIdentifier.getTotalMemroySizeInGigabyte());
		worker.setUuid(ComputerIdentifier.generateLicenseKey());
		ComputerIdentifier.getOperatingSystemName();
		return worker;
	}
	
	private static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) throws SocketException {
	    Enumeration en = NetworkInterface.getNetworkInterfaces();
	    while (en.hasMoreElements()) {
	        NetworkInterface i = (NetworkInterface) en.nextElement();
	        for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
	            InetAddress addr = (InetAddress) en2.nextElement();
	            if (!addr.isLoopbackAddress()) {
	                if (addr instanceof Inet4Address) {
	                    if (preferIPv6) {
	                        continue;
	                    }
	                    return addr;
	                }
	                if (addr instanceof Inet6Address) {
	                    if (preferIpv4) {
	                        continue;
	                    }
	                    return addr;
	                }
	            }
	        }
	    }
	    return null;
	}

	public void initSystemProperties() {
		if (!bInited){
			try {
				architecture = System.getProperty("os.arch");
				cores = Runtime.getRuntime().availableProcessors();
				name = InetAddress.getLocalHost().getHostName();
				hostname = InetAddress.getLocalHost().getHostName();
				
				//use hard code, for some vpn context.
				if (bUseHardCodedIP){
					ipAddress = hardCodedIPAddress;//"192.168.101.252";"10.0.100.181";
					macAddress = hardCodedMacAddress;//"54-EE-77-41-FC-26";
				}
				else{
					try {
						InetAddress address = PlatformUtil.windows() ? InetAddress.getByName(hostname) : getFirstNonLoopbackAddress(true, false);
						ipAddress = address.getHostAddress();
						NetworkInterface network = NetworkInterface.getByInetAddress(address);
						byte[] mac = network.getHardwareAddress();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < mac.length; i++) {
							sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
						}
						macAddress = sb.toString();
					} catch (SocketException e) {
						e.printStackTrace();
					}
				}
				
				String ipFromRemote = serverCommunicator.getWorkerIPFromManager();
				if (ipFromRemote != null && !ipAddress.equals(ipFromRemote)) {
					if (ipFromRemote != ipAddress) {
						System.err.println("system ip is:" + ipAddress);
						System.err.println("atm told ip is:" + ipFromRemote);
					}
				}
				
				bInited = true;
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
}
