package com.meowlomo.vmc.jersey.exception.mapper;

import javax.ws.rs.core.MediaType;

import com.meowlomo.vmc.jersey.exception.CustomeExceptionProperties;
import com.meowlomo.vmc.model.MeowlomoErrorResponse;
import com.meowlomo.vmc.model.MeowlomoResponse;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;

public class CustomeExceptionMapperUtil {
	
	public static Response genResponse(CustomeExceptionProperties properties, Logger logger){
		MeowlomoErrorResponse restErrorResponse = new MeowlomoErrorResponse(
				properties.getStatus(),
				//type
				properties.getType(), 
				//message
				properties.getMessage(), 
				//developer message
				properties.getDeveloperMessage(), 
				properties.getCode(),
				properties.getUuid());

		MeowlomoResponse response = new MeowlomoResponse(null, null, restErrorResponse);

		logger.error(response.toString());
		
		return Response.status(restErrorResponse.getStatusCode())
				.entity(response)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
