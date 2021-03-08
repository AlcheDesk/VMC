package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;

import com.meowlomo.vmc.config.Constant;


public class CustomNotFoundException extends NotFoundException{

	private static final long serialVersionUID = 9115226990509052440L;
	
	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}
	public CustomNotFoundException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_NOT_FOUND
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_NOT_FOUND : Constant.APP_NAME+HttpServletResponse.SC_NOT_FOUND+code)
				, (ex==null? NotFoundException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}
