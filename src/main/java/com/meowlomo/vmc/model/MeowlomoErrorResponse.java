package com.meowlomo.vmc.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Error")
@JsonInclude(Include.NON_NULL)
public class MeowlomoErrorResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3695033558704548775L;
    private Integer statusCode;
    private String type;
    private String message;
    private String developerMessage;
    private String code;
    private UUID uuid;
    private ZonedDateTime date;

    public MeowlomoErrorResponse() {
    }

    public MeowlomoErrorResponse(Integer statusCode, String type, String message, String developerMessage, String code,
            UUID uuid) {
        this.statusCode = statusCode;
        this.type = type;
        this.message = message;
        this.developerMessage = developerMessage;
        this.code = code;
        this.uuid = uuid;
        this.date = ZonedDateTime.now();
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", code=").append(code);
        sb.append(", developerMessage=").append(developerMessage);
        sb.append(", message=").append(message);
        sb.append(", statusCode=").append(statusCode);
        sb.append(", type=").append(type);
        sb.append(", uuid=").append(uuid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}
