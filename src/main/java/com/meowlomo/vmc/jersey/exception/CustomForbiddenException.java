package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;

import com.meowlomo.vmc.config.Constant;


public class CustomForbiddenException extends ForbiddenException{

	private static final long serialVersionUID = 9115226990509052440L;

	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}
	
	public CustomForbiddenException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_FORBIDDEN
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_FORBIDDEN : Constant.APP_NAME+HttpServletResponse.SC_FORBIDDEN+code)
				, (ex==null? ForbiddenException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}
