package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.jersey.exception.CustomNotSupportedException;

@Component
@Provider
public class CustomNotSupportedExceptionMapper  implements ExceptionMapper<CustomNotSupportedException> {

	private static final Logger logger = LoggerFactory.getLogger(CustomNotSupportedExceptionMapper.class);
	
	@Override
	public Response toResponse(CustomNotSupportedException exception) {
		return CustomeExceptionMapperUtil.genResponse(exception.getProperties(), logger);
	}

}
