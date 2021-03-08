package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.InternalServerErrorException;

import com.meowlomo.vmc.config.Constant;


public class CustomInternalServerErrorException extends InternalServerErrorException{

	private static final long serialVersionUID = 9115226990509052440L;

	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}
	
	public CustomInternalServerErrorException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_INTERNAL_SERVER_ERROR
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_INTERNAL_SERVER_ERROR : Constant.APP_NAME+HttpServletResponse.SC_INTERNAL_SERVER_ERROR+code)
				, (ex==null? InternalServerErrorException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}

