package com.meowlomo.vmc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonRootName(value = "Response")
@JsonInclude(Include.NON_NULL)
public class MeowlomoResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3160914001817823841L;
    private ObjectNode metadata;
    private Object data;
    private Object error;

    public MeowlomoResponse() {
    }

    public MeowlomoResponse(ObjectNode metadata, Object data, MeowlomoErrorResponse restErrorResponse) {
        setData(data);
        setError(restErrorResponse);
        setMetadata(metadata);
    }

    public ObjectNode getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectNode metadata) {
        if (metadata == null) {
            this.metadata = JsonNodeFactory.instance.objectNode();
        }
        else {
            this.metadata = metadata;
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data == null) {
            this.data = JsonNodeFactory.instance.arrayNode();
        }
        else {
            if (data instanceof List<?>) {
                this.data = data;
            }
            else {
                List<Object> jsonArray = new ArrayList<>();
                jsonArray.add(data);
                this.data = jsonArray;
            }
        }
    }

    public Object getError() {
        return error;
    }

    public void setError(MeowlomoErrorResponse restErrorResponse) {
        if (restErrorResponse == null) {
            this.error = JsonNodeFactory.instance.objectNode();
        }
        else {
            this.error = restErrorResponse;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", metadata=").append(metadata);
        sb.append(", data=").append(data);
        sb.append(", error=").append(error);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
