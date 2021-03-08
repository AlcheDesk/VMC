package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.jersey.exception.CustomServiceUnavailableException;

@Component
@Provider
public class CustomServiceUnavailableExceptionMapper  implements ExceptionMapper<CustomServiceUnavailableException> {

	private static final Logger logger = LoggerFactory.getLogger(CustomServiceUnavailableExceptionMapper.class);
	
	@Override
	public Response toResponse(CustomServiceUnavailableException exception) {
		return CustomeExceptionMapperUtil.genResponse(exception.getProperties(), logger);
	}

}
