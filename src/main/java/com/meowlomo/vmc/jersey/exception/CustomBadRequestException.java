package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;

import com.meowlomo.vmc.config.Constant;


public class CustomBadRequestException extends BadRequestException{

	private static final long serialVersionUID = 9115226990509052440L;

	private CustomeExceptionProperties properties;

	public CustomeExceptionProperties getProperties() {
		return properties;
	}

	public CustomBadRequestException(Exception ex, String message, String developerMessage, int httpCode, String code, UUID uuid) {
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, 399 < httpCode ? httpCode : HttpServletResponse.SC_BAD_REQUEST
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_BAD_REQUEST : Constant.APP_NAME+HttpServletResponse.SC_NOT_FOUND+code)
				, (ex==null? BadRequestException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}

