package com.meowlomo.vmc.core;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

public class ComputerIdentifier{

    private static final Logger logger = LoggerFactory.getLogger(ComputerIdentifier.class); 

    static SystemInfo systemInfo = new SystemInfo();

    public static UUID generateLicenseKey() {

        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        String model = hardwareAbstractionLayer.getComputerSystem().getModel();
        String manufacturer = hardwareAbstractionLayer.getComputerSystem().getManufacturer();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        String processorSerialNumber = centralProcessor.getProcessorID();
        String processorIdentifier = centralProcessor.getIdentifier();
        int processors = centralProcessor.getLogicalProcessorCount();
        String ip = SystemProperties.ipAddress;//null;
//        try {
//            ip = Inet4Address.getLocalHost().getHostAddress();
//        }
//        catch (UnknownHostException e) {
//
//        }
        
        String delimiter = "#";

        String idString =
                manufacturer +
                delimiter +
                model +
                delimiter +
                processorSerialNumber +
                delimiter +
                processorIdentifier +
                delimiter +
                processors +
                delimiter +
                (ip == null ? "meowlomo":ip);
        logger.info("System id string = "+idString);
        return UUID.nameUUIDFromBytes(idString.getBytes());
    }

    public static int getTotalMemroySizeInGigabyte() {   	
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        GlobalMemory memory = hardwareAbstractionLayer.getMemory();
        long memorySizeInBytes = memory.getTotal();
        int memorySizeInGigabyte = (int) Math.round(memorySizeInBytes/1073741824.000);
        return memorySizeInGigabyte;
    } 

    public static String getMacAddress() {   	
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        NetworkIF[] interfaces = hardwareAbstractionLayer.getNetworkIFs();        
        return interfaces[0].getMacaddr();
    }   

    public static String getOperatingSystemName() {
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        String systemName = SystemInfo.getCurrentPlatformEnum().toString()+" "+operatingSystem.getVersion().getVersion();
        if(systemName.toUpperCase().contains("MAC")) {
            return SystemInfo.getCurrentPlatformEnum().toString();
        }
        else {
            return systemName;
        }
    }
}