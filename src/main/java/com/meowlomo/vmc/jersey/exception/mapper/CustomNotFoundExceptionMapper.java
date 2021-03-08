package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.jersey.exception.CustomNotFoundException;

@Component
@Provider
public class CustomNotFoundExceptionMapper  implements ExceptionMapper<CustomNotFoundException> {

	private static final Logger logger = LoggerFactory.getLogger(CustomNotFoundExceptionMapper.class);
	
	@Override
	public Response toResponse(CustomNotFoundException exception) {
		return CustomeExceptionMapperUtil.genResponse(exception.getProperties(), logger);
	}

}

