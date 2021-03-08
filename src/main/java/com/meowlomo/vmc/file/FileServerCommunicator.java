package com.meowlomo.vmc.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.config.FileServiceApi;
import com.meowlomo.vmc.config.RuntimeVariables;
import com.meowlomo.vmc.util.MOCommonUtil;

@Component
public class FileServerCommunicator {
	
	private static final Logger logger = LoggerFactory.getLogger(FileServerCommunicator.class);
	
    @Autowired
    FileServiceApi fileServiceApi;
	
	@Autowired
	RuntimeVariables runtimeVariables = new RuntimeVariables();
	
	public boolean saveExecutionLogRemote(long taskID, ObjectNode jsonExecutionLog){
		FileService fs = fileServiceApi.createService();
		
		//TODO
		java.nio.file.Path path = Paths.get("test/folder/sdfasdfasdf/test.txt");
		fs.create(path, "sdjfalsjfdlasjdflasjdflasjkdflajslfdjl".getBytes(Charset.forName("UTF-8")), false);
		
		return false;
	}
	
	public boolean saveExecutionLog(long taskID, ObjectNode jsonExecutionLog){
		PrintStream out = null;
		String localPath = null;
		String networkPath = null;
		try{
			logger.info("Saving execution log for task ["+taskID+"]");		
			localPath = System.getProperty("user.home")+
					File.separator+"tem_temp"+
					File.separator+taskID+
					File.separator+"execution.log";
			logger.debug("Saving execution log for task ["+taskID+"] to "+localPath);
			File localTempFile= new File(localPath);
			if(localTempFile.exists()){
				//remove it, the recreate it
				localTempFile.delete();
				localTempFile.createNewFile();
			}else{
				localTempFile.getParentFile().mkdirs();
				localTempFile.createNewFile();
			}
			if(!localTempFile.exists()){
				logger.error("Local temp file for execution log is not created : "+localTempFile.getPath());
			}
			
			out = new PrintStream(new FileOutputStream(localPath),true,"UTF-8");
			boolean foundContent = false;
			//get the input stream first 
			if(jsonExecutionLog.get("inputStream") != null){
				String inputStreamContent = jsonExecutionLog.get("inputStream").asText();
				out.println("==========Input Stream Output==========\r\n");
				out.println(inputStreamContent);
				foundContent = true;
			}

			//get the error stream
			if(jsonExecutionLog.get("errorStream") != null){
				String errorStreamContent = jsonExecutionLog.get("errorStream").asText();
				out.println("==========Error Stream Output==========\r\n");
				out.println(errorStreamContent);
				foundContent = true;
			}
			
			//not content, will print out the json object
			if(!foundContent){
				String content = jsonExecutionLog.toString();
				out.println(content);
			}
			out.close();
			MOCommonUtil.sleepSeconds(2);
			//copy the file to network drive
			networkPath = runtimeVariables.getFileServerMountPoint()+"/log/task/"+taskID+"/execution.log";
			File networkDriveCopyFile = new File(networkPath);
			//check if the file exists
			if(networkDriveCopyFile.exists()){
				logger.info("task ["+taskID+"] found existing execution log : "+ networkPath);
				//rename the current one.
				Path networkCopyPath = Paths.get(networkPath);
				BasicFileAttributes attr = Files.readAttributes(networkCopyPath, BasicFileAttributes.class);
				String fileModifiedDateTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(attr.lastModifiedTime().toMillis()));
				File newNetworkDriveCopyFile = new File(runtimeVariables.getFileServerMountPoint()+"/log/task/"+taskID+"/execution_"+fileModifiedDateTime+".log");
				logger.info("task ["+taskID+"] execution log ["+networkDriveCopyFile.getAbsolutePath()+"] will be renamed to " + newNetworkDriveCopyFile.getAbsolutePath());
				//copy the new file to the location.				
				Files.copy(networkDriveCopyFile.toPath(), newNetworkDriveCopyFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				logger.info("task ["+taskID+"] renamed execution log ["+newNetworkDriveCopyFile.getAbsolutePath()+"] created ["+newNetworkDriveCopyFile.exists()+"]");
				Files.copy(localTempFile.toPath(), networkDriveCopyFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				logger.info("task ["+taskID+"] old execution log ["+newNetworkDriveCopyFile.getAbsolutePath()+"] created ["+newNetworkDriveCopyFile.exists()+"]");
				logger.info("task ["+taskID+"] execution log ["+networkDriveCopyFile.getAbsolutePath()+"] created ["+networkDriveCopyFile.exists()+"]");
			}
			else{
				logger.info("task ["+taskID+"] Copying execution log.");
				networkDriveCopyFile.getParentFile().mkdirs();
				networkDriveCopyFile.createNewFile();
				Files.copy(localTempFile.toPath(), networkDriveCopyFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			}
			
			//delete the local copy
			try{
				if(localTempFile.getParentFile().isDirectory()){
//					FileUtils.deleteDirectory(localTempFile.getParentFile());
					logger.info("Local execution log directory : "+localTempFile.getParentFile().getAbsolutePath()+" is deleted successfully.");
				}
				else{
//					FileUtils.forceDelete(localTempFile);
					logger.info("Local execution log file : "+localTempFile.getAbsolutePath()+" is deleted successfully.");
				}
			}catch(SecurityException e){
				logger.error("Can't delete the local execiton log from ["+localPath+"]",e);
			}
			
		    return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Can't write execution log to local file "+localPath, e);
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Can't copy the execiton log from ["+localPath+"] to ["+networkPath+"]",e);
			return false;
		} finally {
			out.close();		
		}
	}
	
	public static boolean deleteTempFolder(){
		//get the temp folder path
		String tempFolderPath = System.getProperty("java.io.tmpdir");
		File tempFolder = new File (tempFolderPath);
		//check the folder exists 
		if(!tempFolder.exists()){
			logger.error("FileServerCommunicator::deleteTempFolder, the temp folder doean't exists "+tempFolderPath);
			return false;
		}
		else if(!tempFolder.isDirectory()){
			logger.error("FileServerCommunicator::deleteTempFolder, the temp folder path is not a directory "+tempFolderPath);
			return false;
		}
		else if(tempFolder.canWrite()){
			logger.error("FileServerCommunicator::deleteTempFolder, the temp folder is not writable "+tempFolderPath);
			return false;
		}
		else{
			//clean the folder
//			try {
//				FileUtils.cleanDirectory(tempFolder);
				tempFolder.delete();
				return true;
//			} catch (IOException e) {
//				e.printStackTrace();
//				logger.error("FileServerCommunicator::deleteTempFolder, get error on deleting the temp folder",e);
//				return false;
//			}
		}
		
	}
	
	public boolean deleteFilesInTempOlderThan(int days){
		String tempFolderPath = System.getProperty("java.io.tmpdir");
		File tempFolder = new File (tempFolderPath);
		File[] fileList = tempFolder.listFiles();
		if(fileList != null){
			for(File file : fileList){
				long diff = new Date().getTime() - file.lastModified();
				if(diff > days * 24 * 60 * 60 * 1000){
					file.delete();
//					if(file.isDirectory()){
//						try {
//							FileUtils.deleteDirectory(file);
//						} catch (IOException e) {
//							logger.error("FileServerCommunicator::deleteTempFolder, get error on cleaning up tem folder older by "+days+" days",e);
//							return false;
//						}
//					}else{
//						file.delete();
//					}
				}
			}
			return true;
		}
		else{
			logger.error("FileServerCommunicator::deleteTempFolder, the tmep path is not a folder "+tempFolderPath);
			return false;
		}
	}
}
