package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAcceptableException;

import com.meowlomo.vmc.config.Constant;


public class CustomNotAcceptableException extends NotAcceptableException{

	private static final long serialVersionUID = 9115226990509052440L;

	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}
	
	public CustomNotAcceptableException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_NOT_ACCEPTABLE
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_NOT_ACCEPTABLE : Constant.APP_NAME+HttpServletResponse.SC_NOT_ACCEPTABLE+code)
				, (ex==null? NotAcceptableException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}
