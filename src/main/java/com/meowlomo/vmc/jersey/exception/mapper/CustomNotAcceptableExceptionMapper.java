package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.jersey.exception.CustomNotAcceptableException;

@Component
@Provider
public class CustomNotAcceptableExceptionMapper  implements ExceptionMapper<CustomNotAcceptableException> {

	private static final Logger logger = LoggerFactory.getLogger(CustomNotAcceptableExceptionMapper.class);
	
	@Override
	public Response toResponse(CustomNotAcceptableException exception) {
		return CustomeExceptionMapperUtil.genResponse(exception.getProperties(), logger);
	}

}
