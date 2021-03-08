package com.meowlomo.vmc.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.SystemUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;


public class InMomeryFileSystem {
    
    private static FileSystem fs;
    
    public InMomeryFileSystem() {
        if(SystemUtils.IS_OS_LINUX) {
            fs = Jimfs.newFileSystem(Configuration.unix());
        }
        else if(SystemUtils.IS_OS_WINDOWS) {
            fs = Jimfs.newFileSystem(Configuration.unix());
        }
        else if(SystemUtils.IS_OS_MAC_OSX) {
            fs = Jimfs.newFileSystem(Configuration.osX());
        }
        else {
           fs = Jimfs.newFileSystem(Configuration.unix());
        }
        
    }
    
    public boolean createFolder(String folderPath) {
        try {
            Files.createDirectory(fs.getPath(folderPath));
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean createFile(String filePathString, String fileContent) {
        Path filePath = fs.getPath(filePathString);
        try {
            Files.write(filePath, ImmutableList.of(fileContent), StandardCharsets.UTF_8);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean readFile(String filePathString) {
        Path filePath = fs.getPath(filePathString);
        try {
            Files.write(filePath, ImmutableList.of("test content"), StandardCharsets.UTF_8);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean exists(String path) {
        Path filePath = fs.getPath(path);
        return Files.isDirectory(filePath);
    }

}
