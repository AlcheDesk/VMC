package com.meowlomo.vmc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class InstructionResult implements Serializable {
    private Long id;

    private String action;

    private Date createdAt;

    private Date updatedAt;

    private String status;

    private String log;

    private Boolean finished;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode instruction;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode data;

    private Long runId;

    private String runType;

    private Date startAt;

    private Date endAt;

    private String logicalOrderIndex;

    private String inputData;

    private String inputType;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode inputParameter;

    private String outputData;

    private String outputType;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode outputParameter;

    private String target;

    private List<File> files;

    private List<StepLog> stepLogs;

    private List<ExecutionLog> executionLogs;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log == null ? null : log.trim();
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public JsonNode getInstruction() {
        return instruction;
    }

    public void setInstruction(JsonNode instruction) {
        this.instruction = instruction;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
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

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getLogicalOrderIndex() {
        return logicalOrderIndex;
    }

    public void setLogicalOrderIndex(String logicalOrderIndex) {
        this.logicalOrderIndex = logicalOrderIndex == null ? null : logicalOrderIndex.trim();
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData == null ? null : inputData.trim();
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType == null ? null : inputType.trim();
    }

    public JsonNode getInputParameter() {
        return inputParameter;
    }

    public void setInputParameter(JsonNode inputParameter) {
        this.inputParameter = inputParameter;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData == null ? null : outputData.trim();
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType == null ? null : outputType.trim();
    }

    public JsonNode getOutputParameter() {
        return outputParameter;
    }

    public void setOutputParameter(JsonNode outputParameter) {
        this.outputParameter = outputParameter;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<StepLog> getStepLogs() {
        return stepLogs;
    }

    public void setStepLogs(List<StepLog> stepLogs) {
        this.stepLogs = stepLogs;
    }

    public List<ExecutionLog> getExecutionLogs() {
        return executionLogs;
    }

    public void setExecutionLogs(List<ExecutionLog> executionLogs) {
        this.executionLogs = executionLogs;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (that == null) { return false; }
        if (getClass() != that.getClass()) { return false; }
        InstructionResult other = (InstructionResult) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAction() == null ? other.getAction() == null : this.getAction().equals(other.getAction()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null
                        : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getLog() == null ? other.getLog() == null : this.getLog().equals(other.getLog()))
                && (this.getFinished() == null ? other.getFinished() == null
                        : this.getFinished().equals(other.getFinished()))
                && (this.getInstruction() == null ? other.getInstruction() == null
                        : this.getInstruction().equals(other.getInstruction()))
                && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()))
                && (this.getRunId() == null ? other.getRunId() == null : this.getRunId().equals(other.getRunId()))
                && (this.getRunType() == null ? other.getRunType() == null
                        : this.getRunType().equals(other.getRunType()))
                && (this.getStartAt() == null ? other.getStartAt() == null
                        : this.getStartAt().equals(other.getStartAt()))
                && (this.getEndAt() == null ? other.getEndAt() == null : this.getEndAt().equals(other.getEndAt()))
                && (this.getLogicalOrderIndex() == null ? other.getLogicalOrderIndex() == null
                        : this.getLogicalOrderIndex().equals(other.getLogicalOrderIndex()))
                && (this.getInputData() == null ? other.getInputData() == null
                        : this.getInputData().equals(other.getInputData()))
                && (this.getInputType() == null ? other.getInputType() == null
                        : this.getInputType().equals(other.getInputType()))
                && (this.getInputParameter() == null ? other.getInputParameter() == null
                        : this.getInputParameter().equals(other.getInputParameter()))
                && (this.getOutputData() == null ? other.getOutputData() == null
                        : this.getOutputData().equals(other.getOutputData()))
                && (this.getOutputType() == null ? other.getOutputType() == null
                        : this.getOutputType().equals(other.getOutputType()))
                && (this.getOutputParameter() == null ? other.getOutputParameter() == null
                        : this.getOutputParameter().equals(other.getOutputParameter()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAction() == null) ? 0 : getAction().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getLog() == null) ? 0 : getLog().hashCode());
        result = prime * result + ((getFinished() == null) ? 0 : getFinished().hashCode());
        result = prime * result + ((getInstruction() == null) ? 0 : getInstruction().hashCode());
        result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        result = prime * result + ((getRunId() == null) ? 0 : getRunId().hashCode());
        result = prime * result + ((getRunType() == null) ? 0 : getRunType().hashCode());
        result = prime * result + ((getStartAt() == null) ? 0 : getStartAt().hashCode());
        result = prime * result + ((getEndAt() == null) ? 0 : getEndAt().hashCode());
        result = prime * result + ((getLogicalOrderIndex() == null) ? 0 : getLogicalOrderIndex().hashCode());
        result = prime * result + ((getInputData() == null) ? 0 : getInputData().hashCode());
        result = prime * result + ((getInputType() == null) ? 0 : getInputType().hashCode());
        result = prime * result + ((getInputParameter() == null) ? 0 : getInputParameter().hashCode());
        result = prime * result + ((getOutputData() == null) ? 0 : getOutputData().hashCode());
        result = prime * result + ((getOutputType() == null) ? 0 : getOutputType().hashCode());
        result = prime * result + ((getOutputParameter() == null) ? 0 : getOutputParameter().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", action=").append(action);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", status=").append(status);
        sb.append(", log=").append(log);
        sb.append(", finished=").append(finished);
        sb.append(", instruction=").append(instruction);
        sb.append(", data=").append(data);
        sb.append(", runId=").append(runId);
        sb.append(", runType=").append(runType);
        sb.append(", startAt=").append(startAt);
        sb.append(", endAt=").append(endAt);
        sb.append(", logicalOrderIndex=").append(logicalOrderIndex);
        sb.append(", inputData=").append(inputData);
        sb.append(", inputType=").append(inputType);
        sb.append(", inputParameter=").append(inputParameter);
        sb.append(", outputData=").append(outputData);
        sb.append(", outputType=").append(outputType);
        sb.append(", outputParameter=").append(outputParameter);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}