package com.meowlomo.vmc.jersey.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.jersey.exception.CustomBadRequestException;
import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.Task;

@Component
@Path("/cmd")
public class CommandResource {
	private final Logger logger = LoggerFactory.getLogger(CommandResource.class);

	@Context
    HttpServletRequest httpRequest;
	
	@POST
	@Path("post")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public String processCmd(@QueryParam("cmd") String cmd) {
		String msg = "Received CMD [" + cmd + "]";
		logger.info(msg);
		return msg;
	}

	@GET
	@Path("post")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public String processGet() {
		String msg = "Please use POST method to send the info";
		logger.info(msg);
		return msg;
	}

	@POST
	@Path("form")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public MeowlomoResponse createCustomer(@FormParam("email") String email, @FormParam("password") String password,
			@FormParam("domin") String domin) {
		System.err.println(email);
		System.err.println(password);
		System.err.println(domin);
		if (null == email || null == password || null == domin)
			throw new CustomBadRequestException(null, "参数不匹配", "", HttpServletResponse.SC_FORBIDDEN, "VMC08B",
					UUID.randomUUID());

		ObjectNode metadata = JsonNodeFactory.instance.objectNode();
		// put status
		metadata.put("email", email);
		metadata.put("password", password);
		metadata.put("domin", domin);
		metadata.put("calling ip", getIPInfo());
		MeowlomoResponse response = new MeowlomoResponse(metadata, new ArrayList<Task>(), null);

		return response;
	}
	
	@POST
	@Path("json")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
	public MeowlomoResponse createCustomerJson(final JsonNode customer) {

		ObjectNode metadata = JsonNodeFactory.instance.objectNode();
		// put status
		metadata.put("email", customer.get("email").asText());
		metadata.put("password", customer.get("password").asText());
		metadata.put("domin", customer.get("domin").asText());
		metadata.put("calling ip", getIPInfo());
		metadata.put("calling cookie", getClientCookie());
		ArrayNode workers = JsonNodeFactory.instance.arrayNode();
		for(int index = 0; index < 3; ++index) {
			ObjectNode worker = JsonNodeFactory.instance.objectNode();
			worker.put("id", index + 123);
			worker.put("name", Arrays.asList(new String[] {"Emma", "Lucy", "Lily"}).get(index));
			worker.put("salaryAmount", Arrays.asList(new String[] {"39999", "3999", "9999"}).get(index));
			workers.add(worker);
		}
		metadata.set("worker", workers);
		
		MeowlomoResponse response = new MeowlomoResponse(metadata, new ArrayList<Task>(), null);

		return response;
	}
	
	private String getIPInfo() {
		String clientIP = httpRequest.getHeader("X-FORWARDED-FOR");
		if (clientIP == null) {
			clientIP = httpRequest.getRemoteAddr();  
		}
		String clientIPInfo = " IP is " + ((null == clientIP || clientIP.isEmpty()) ? "null" : clientIP);
		return clientIPInfo;
	}
	
	private String getClientCookie() {
		Cookie[] cookie = httpRequest.getCookies();
		String cookieString = "";
		if (null == cookie)
			cookieString = "null";
		else
			for (int i = 0; i < cookie.length; i++) {
				Cookie cook = cookie[i];
				cookieString += (cook.getName() + "=" + cook.getValue() + " ;");
			}
		return cookieString;
	}
}
