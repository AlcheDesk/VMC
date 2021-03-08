package com.meowlomo.vmc.abandoned;
//package com.meowlomo.vmc.config;
//
//import java.io.IOException;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
//import com.hierynomus.smbj.SMBClient;
//import com.hierynomus.smbj.auth.AuthenticationContext;
//import com.hierynomus.smbj.connection.Connection;
//import com.hierynomus.smbj.session.Session;
//import com.hierynomus.smbj.share.DiskShare;
//
//import jcifs.smb.NtlmPasswordAuthentication;
//import jcifs.smb.SmbSession;
//
//@Component
//public class FileServiceConfig {
//
//	//check the type of the fiel server
//	@Value("${meowlomo.cofnig.service.file.type}")
//	private  String serviceType ;
//	@Value("${meowlomo.cofnig.service.file.username}")
//	private  String username;
//	@Value("${meowlomo.cofnig.service.file.password}")
//	private  String password;
//	@Value("${meowlomo.cofnig.service.file.hostname}")
//	private String hostname;
//	@Value("${meowlomo.cofnig.service.file.domain}")
//	private String serverDomain;
//
//
//	//smb
//	Session smbSession;
//
//	@PostConstruct
//	public void init()  {
//		//check the type of the service
//		if(this.serviceType.equalsIgnoreCase("smb") || this.serviceType.equalsIgnoreCase("cifs")) {
//			SMBClient client = new SMBClient();
//
//			try (Connection connection = client.connect(this.hostname)) {
//				AuthenticationContext auth = new AuthenticationContext(this.username, this.password.toCharArray(), this.serverDomain);
//				this.smbSession = connection.authenticate(auth);
//
//				// Connect to Share
//				try (DiskShare share = (DiskShare) session.connectShare("SHARENAME")) {
//					for (FileIdBothDirectoryInformation f : share.list("FOLDER", "*.TXT")) {
//						System.out.println("File : " + f.getFileName());
//					}
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//	
//    public  <S> S createService() {
//    	if(this.serviceType.equalsIgnoreCase("smb") || this.serviceType.equalsIgnoreCase("cifs")) {
//    		return this.smbSession;
//    	}
//    }
//}
