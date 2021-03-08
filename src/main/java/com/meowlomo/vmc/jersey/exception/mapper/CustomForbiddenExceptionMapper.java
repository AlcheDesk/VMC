package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.jersey.exception.CustomForbiddenException;

@Component
@Provider
public class CustomForbiddenExceptionMapper  implements ExceptionMapper<CustomForbiddenException> {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomForbiddenExceptionMapper.class);
	
	@Override
	public Response toResponse(CustomForbiddenException exception) {
		return CustomeExceptionMapperUtil.genResponse(exception.getProperties(), logger);
	}

}
