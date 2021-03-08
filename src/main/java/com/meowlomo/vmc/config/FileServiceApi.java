package com.meowlomo.vmc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.file.FileService;
import com.meowlomo.vmc.file.impl.SMBFileService;

@Component
public class FileServiceApi {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceApi.class);

    // check the type of the file server
    @Value("${meowlomo.config.service.file.type}")
    private String serviceType;
    @Value("${meowlomo.config.service.file.path}")
    private String path;
    @Value("${meowlomo.config.service.file.username}")
    private String username;
    @Value("${meowlomo.config.service.file.password}")
    private String password;
    @Value("${meowlomo.config.service.file.hostname}")
    private String hostname;
    @Value("${meowlomo.config.service.file.domain}")
    private String serverDomain;
    @Value("${meowlomo.config.service.file.sharename}")
    private String sharename;

    public FileService createService() {
        if (this.serviceType.equalsIgnoreCase("smb") || this.serviceType.equalsIgnoreCase("cifs")) {
            logger.info("create cifs file service by config");
            return new SMBFileService(hostname, username, password, serverDomain, sharename);
        }
        else {
            logger.info("create cifs file service by default");
            return new SMBFileService(hostname, username, password, serverDomain, sharename);
        }
    }

    public FileService createService(String username, String password) {
        if (this.serviceType.equalsIgnoreCase("smb") || this.serviceType.equalsIgnoreCase("cifs")) {
            logger.info("create cifs file service by config");
            return new SMBFileService(hostname, username, password, serverDomain, sharename);
        }
        else {
            logger.info("create cifs file service by default");
            return new SMBFileService(hostname, username, password, serverDomain, sharename);
        }
    }
}
