package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ServiceUnavailableException;

import com.meowlomo.vmc.config.Constant;


public class CustomServiceUnavailableException extends ServiceUnavailableException{

	private static final long serialVersionUID = 9115226990509052440L;
	
	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}

	public CustomServiceUnavailableException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_SERVICE_UNAVAILABLE
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_SERVICE_UNAVAILABLE : Constant.APP_NAME+HttpServletResponse.SC_SERVICE_UNAVAILABLE+code)
				, (ex==null? ServiceUnavailableException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}
