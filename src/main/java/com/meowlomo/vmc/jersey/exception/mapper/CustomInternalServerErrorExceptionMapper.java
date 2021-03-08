package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.jersey.exception.CustomInternalServerErrorException;

@Component
@Provider
public class CustomInternalServerErrorExceptionMapper  implements ExceptionMapper<CustomInternalServerErrorException> {

	private static final Logger logger = LoggerFactory.getLogger(CustomInternalServerErrorExceptionMapper.class);
	
	@Override
	public Response toResponse(CustomInternalServerErrorException exception) {
		return CustomeExceptionMapperUtil.genResponse(exception.getProperties(), logger);
	}
}
