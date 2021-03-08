package com.meowlomo.vmc.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExecutionLog implements Serializable {
    private Long id;

    private String message;

    private Date updatedAt;

    private Date createdAt;

    private String logLevel;

    private Long instructionResultId;

    private Long runId;

    private String runType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel == null ? null : logLevel.trim();
    }

    public Long getInstructionResultId() {
        return instructionResultId;
    }

    public void setInstructionResultId(Long instructionResultId) {
        this.instructionResultId = instructionResultId;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType == null ? null : runType.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (that == null) { return false; }
        if (getClass() != that.getClass()) { return false; }
        ExecutionLog other = (ExecutionLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getMessage() == null ? other.getMessage() == null
                        : this.getMessage().equals(other.getMessage()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null
                        : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getLogLevel() == null ? other.getLogLevel() == null
                        : this.getLogLevel().equals(other.getLogLevel()))
                && (this.getInstructionResultId() == null ? other.getInstructionResultId() == null
                        : this.getInstructionResultId().equals(other.getInstructionResultId()))
                && (this.getRunId() == null ? other.getRunId() == null : this.getRunId().equals(other.getRunId()))
                && (this.getRunType() == null ? other.getRunType() == null
                        : this.getRunType().equals(other.getRunType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getLogLevel() == null) ? 0 : getLogLevel().hashCode());
        result = prime * result + ((getInstructionResultId() == null) ? 0 : getInstructionResultId().hashCode());
        result = prime * result + ((getRunId() == null) ? 0 : getRunId().hashCode());
        result = prime * result + ((getRunType() == null) ? 0 : getRunType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", message=").append(message);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", logLevel=").append(logLevel);
        sb.append(", instructionResultId=").append(instructionResultId);
        sb.append(", runId=").append(runId);
        sb.append(", runType=").append(runType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}