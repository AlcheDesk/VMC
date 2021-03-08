package com.meowlomo.vmc.file.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.Directory;
import com.hierynomus.smbj.share.DiskShare;
import com.meowlomo.vmc.file.FileService;

public class SMBFileService implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(SMBFileService.class);

    // smb session
    private static SMBClient client;
    private static Session session;
    private String sharename;
    private String hostname;


    public SMBFileService(String hostname, String username, String password, String serverDomain, String sharename) {
        SMBFileService.client = new SMBClient();
        this.sharename = sharename;
        this.hostname = hostname;
        try {
            Connection connection = client.connect(hostname);
            AuthenticationContext auth = new AuthenticationContext(username, password.toCharArray(), serverDomain);
            SMBFileService.session = connection.authenticate(auth);
            // this.share = (DiskShare) this.session.connectShare(sharename);
        }
        catch (IOException e) {
            logger.error("error on create connection to smb/cifs share to " + hostname, e);
        }
        catch (Exception e) {
            logger.error("error on create connection to smb/cifs share to " + hostname, e);
        }
    }

    @Override
    public boolean create(Path path, byte[] fileContent, boolean overwrite) {
        // Session session = this.connection.authenticate(this.auth);
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        // create the folder first
        Path parent = path.getParent();
        if (parent != null && createDirectory(parent)) {
            // write the file
            com.hierynomus.smbj.share.File fileOnshare;
            // String fileFullPath = path.toString()+File.separator+file.getName();
            if (!share.fileExists(path.toString())) {
                fileOnshare = share.openFile(
                		path.toString()
                		, new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL))
                		, new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)), SMB2ShareAccess.ALL
                		, SMB2CreateDisposition.FILE_CREATE
                		, new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
            }
            // the file exists
            else {
                if (!overwrite) {
                    logger.error("file exists " + path.toString());
                    return false;
                }
                else {
                    share.rm(path.toString());
                    fileOnshare = share.openFile(
                    		path.toString()
                    		, new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL))
                    		, new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL))
                    		, SMB2ShareAccess.ALL
                    		, SMB2CreateDisposition.FILE_CREATE
                    		, new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
                }
            }

            // the file is created on share
            if (fileOnshare == null) {
                logger.error("error on creating file content to the share path: " + path.toString());
                return false;
            }
            OutputStream os = fileOnshare.getOutputStream();
            try {
                os.write(fileContent);
                os.close();
            }
            catch (IOException e) {
                logger.error("error on writing file content to the share path: " + path.toString(), e);
                return false;
            }
            return true;
        }
        else if(parent == null) {
            return true;
        }
        else {
            logger.error("error on creating the direcotry path: " + path.toString());
            return false;
        }
    }

    @Override
    public boolean appendToFile(Path path, byte[] content) {
        return false;
    }

    @Override
    public byte[] readFile(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        if (share.fileExists(path.toString())) {
            com.hierynomus.smbj.share.File f = share.openFile(path.toString(),
                    new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
                    new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)), SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN, null);

            try {
                InputStream fileStream = f.getInputStream();
                return org.apache.commons.io.IOUtils.toByteArray(fileStream);
            }
            catch (SMBApiException | IOException e) {
                logger.error("error on creating the direcotry path: " + path.toString());
            }
            return null;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean createDirectory(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        if (!share.folderExists(path.toString())) {
            System.err.println("path not exists");
            // create the folder
            // getthe parent folder
            Path parentDir = path.getParent();
            System.err.println("parent path " + parentDir);
            // 1: parent exists
            if (parentDir != null && share.folderExists(parentDir.toString())) {
                // create the current folder only
                share.mkdir(path.toString());
            }
            // 2: parent not exists
            else if (parentDir != null && !share.folderExists(parentDir.toString())) {
                // create the parent folder then the current
                if (createDirectory(parentDir))
                    share.mkdir(path.toString());
            }
            // no parent folder
            else {
                // just create the current
                share.mkdir(path.toString());
            }

            // check the final result
            if (share.folderExists(path.toString())) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public boolean isReadable(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        com.hierynomus.smbj.share.File fileInfo=null;
        try {
            fileInfo = share.openFile(path.toString(),
                    new HashSet<>(Arrays.asList(AccessMask.GENERIC_READ)),
                    new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
                    new HashSet<>(Arrays.asList(SMB2ShareAccess.FILE_SHARE_READ)), SMB2CreateDisposition.FILE_OPEN, null);
        }catch (SMBApiException e) {

        }

        Directory folderInfo = null;
        try {
            folderInfo = share.openDirectory(path.toString(),
                    new HashSet<>(Arrays.asList(AccessMask.GENERIC_READ)),
                    new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
                    new HashSet<>(Arrays.asList(SMB2ShareAccess.FILE_SHARE_READ)), SMB2CreateDisposition.FILE_OPEN, null);
        }catch(SMBApiException e) {

        }

        if(fileInfo != null || folderInfo != null) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isWritable(Path path) {
//        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);       
//        System.err.println(share.getFileInformation(path.toString()));
//        com.hierynomus.smbj.share.File fileInfo=null;
//        try {
//            fileInfo = share.openFile(path.toString(),
//                    new HashSet<>(Arrays.asList(AccessMask.GENERIC_WRITE)),
//                    new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
//                    new HashSet<>(Arrays.asList(SMB2ShareAccess.FILE_SHARE_WRITE)), SMB2CreateDisposition.FILE_OPEN, null);
//        }catch (SMBApiException e) {
//
//        }
//
//        Directory folderInfo = null;
//        try {
//            folderInfo = share.openDirectory(path.toString(),
//                    new HashSet<>(Arrays.asList(AccessMask.GENERIC_WRITE)),
//                    new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
//                    new HashSet<>(Arrays.asList(SMB2ShareAccess.FILE_SHARE_WRITE)), SMB2CreateDisposition.FILE_OPEN, null);
//        }catch(SMBApiException e) {
//
//        }
//        
//        System.err.println("file "+fileInfo+" folder"+folderInfo);
//        if(fileInfo != null || folderInfo != null) {
//            return true;
//        }
//        else {
//            return false;
//        }
        this.createDirectory(Paths.get("tmp_check_write"));
        return this.exist(Paths.get("tmp_check_write"));
    }

    @Override
    public boolean exist(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        if (share.fileExists(path.toString()))
            return true;
        if (share.folderExists(path.toString()))
            return true;
        return false;
    }

    @Override
    public long getFreeSpace() {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        return share.getShareInformation().getFreeSpace();
    }

    @Override
    public boolean rename(Path originalPath, String newName, boolean overwrite) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        com.hierynomus.smbj.share.File fileInfo = share.openFile(originalPath.toString(),
                new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
                new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)), SMB2ShareAccess.ALL,
                SMB2CreateDisposition.FILE_OPEN, new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
        fileInfo.rename(newName, overwrite);
        if (share.fileExists(originalPath.getParent() + File.separator + newName))
            return true;
        if (share.folderExists(originalPath.getParent() + File.separator + newName))
            return true;
        return false;
    }

    @Override
    public boolean delete(Path path, boolean recursive) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        if (share.fileExists(path.toString()))
            share.rm(path.toString());
        if (share.folderExists(path.toString()))
            share.rmdir(path.toString(), recursive);
        if (share.fileExists(path.toString()))
            return false;
        if (share.folderExists(path.toString()))
            return false;
        return true;
    }

    @Override
    public List<Path> listFile(Path path, String searchPattern) {
        List<Path> filePaths = new ArrayList<Path>();
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        for (FileIdBothDirectoryInformation f : share.list(path.toString(), searchPattern)) {
            filePaths.add(Paths.get(path.toString() + File.separator + f.getFileName()));
        }
        return filePaths;
    }

    @Override
    public boolean isFile(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        if (share.fileExists(path.toString()))
            return share.fileExists(path.toString());
        return false;
    }

    @Override
    public boolean folderExists(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        return share.folderExists(path.toString());
    }

    @Override
    public boolean fileExists(Path path) {
        DiskShare share = (DiskShare) SMBFileService.session.connectShare(sharename);
        return share.folderExists(path.toString());
    }

    @Override
    public boolean accessiable() {
        try {
            return client.connect(this.hostname).isConnected();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public static void main(String[] args) {
//        SMBFileService smb = new SMBFileService("10.0.40.80", "vmcadmin", "atmadmin666", "/", "vmc");
//        smb.createDirectory(Paths.get("tmp_check_write"));
//        System.err.println(smb.exist(Paths.get("tmp_check_write")));
//        smb.create(Paths.get("temp/nice.txt"), "hello".getBytes(), true);
//        System.err.println(smb.readFile(Paths.get("temp/nice.txt")).toString());
//    }

}
