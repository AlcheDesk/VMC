package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.MediaType;

import com.meowlomo.vmc.config.Constant;


public class CustomNotAllowedException extends NotAllowedException{

	private static final long serialVersionUID = 9115226990509052440L;
	
	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}

	public CustomNotAllowedException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		super(message, MediaType.APPLICATION_JSON,new String[] {MediaType.APPLICATION_JSON_PATCH_JSON});
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_METHOD_NOT_ALLOWED
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_METHOD_NOT_ALLOWED : Constant.APP_NAME+HttpServletResponse.SC_METHOD_NOT_ALLOWED+code)
				, (ex==null? NotAllowedException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}
