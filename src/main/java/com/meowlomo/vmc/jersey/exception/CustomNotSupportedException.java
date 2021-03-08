package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotSupportedException;

import com.meowlomo.vmc.config.Constant;


public class CustomNotSupportedException extends NotSupportedException{

	private static final long serialVersionUID = 9115226990509052440L;

	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}

	public CustomNotSupportedException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE : Constant.APP_NAME+HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE+code)
				, (ex==null? NotSupportedException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}

