package com.meowlomo.vmc.jersey.exception;

import java.util.UUID;

public class CustomeExceptionProperties {
	
	private String message;
	private String developerMessage;
	private int status;
	private String code;
	private String type;
	private UUID uuid;

	public CustomeExceptionProperties(String msg, String devMsg, int status, String code, String type, UUID uuid){
		this.message = msg;
		this.developerMessage = devMsg;
		this.status = status;
		this.code = code;
		this.type = type;
		this.uuid = uuid;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

}
