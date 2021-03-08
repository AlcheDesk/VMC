package com.meowlomo.vmc.jersey.exception.mapper;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.config.Constant;
import com.meowlomo.vmc.model.MeowlomoErrorResponse;
import com.meowlomo.vmc.model.MeowlomoResponse;

@Component
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	private static final Logger logger = LoggerFactory.getLogger(NotFoundExceptionMapper.class);
	
	@Override
	public Response toResponse(NotFoundException exception) {
        MeowlomoErrorResponse restErrorResponse = new MeowlomoErrorResponse(
        		HttpServletResponse.SC_NOT_FOUND,
        		//type
        		exception.getClass().getName(), 
        		//message
        		"the uri is not valid", 
        		//developer message
        		exception.getMessage(), 
        		//code, internal error code
        		Constant.APP_NAME+HttpServletResponse.SC_NOT_FOUND,
        		UUID.randomUUID());
        
        MeowlomoResponse response = new MeowlomoResponse(null, null, restErrorResponse);
 
		logger.error(response.toString());
        
        return Response.status(restErrorResponse.getStatusCode())
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
	}
}
