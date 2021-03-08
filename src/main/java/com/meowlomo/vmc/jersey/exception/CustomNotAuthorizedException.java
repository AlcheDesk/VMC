package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;

import com.meowlomo.vmc.config.Constant;

public class CustomNotAuthorizedException extends NotAuthorizedException{

	public CustomNotAuthorizedException(Object challenge, Object[] moreChallenges) {
		super(challenge, moreChallenges);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 9115226990509052440L;

	private CustomeExceptionProperties properties;
	
	public CustomeExceptionProperties getProperties() {
		return properties;
	}

	public CustomNotAuthorizedException(Exception ex, String message, String developerMessage, String code, UUID uuid) {
		super(message, new Object());
		this.properties = new CustomeExceptionProperties(
				message
				, developerMessage
				, HttpServletResponse.SC_UNAUTHORIZED
				, (code == null ? Constant.APP_NAME+HttpServletResponse.SC_UNAUTHORIZED : Constant.APP_NAME+HttpServletResponse.SC_UNAUTHORIZED+code)
				, (ex==null? NotAuthorizedException.class.getName() : ex.getClass().getName())
				, uuid);
	}
}

