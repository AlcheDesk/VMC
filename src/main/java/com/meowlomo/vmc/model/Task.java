package com.meowlomo.vmc.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;

public class Task implements Serializable {
    private Long id;

    private String name;

    @ApiModelProperty(value = "UUID", dataType = "String")
    private UUID uuid;

    private Integer priority;

    private String type;

    private String group;

    private Integer cpuCoreRequired;

    private Integer ramRequired;

    private Integer bandwidthRequired;

    private Date createdAt;

    private Date updatedAt;

    private Boolean interactive;

    private String status;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode parameter;

    private Long timeout;

    private Long jobId;

    private Boolean finished;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode data;

    private String operatingSystem;

    @ApiModelProperty(value = "JSON data", dataType = "String")
    private JsonNode executionResult;

    private Date executionStartAt;

    private Date executionEndAt;

    private Integer maxRetry;

    private Integer retryNumber;

    private Long workerId;

    private Boolean singleton;

    private UUID externalIdentifier;

    private UUID singletonUuid;

    private static final long serialVersionUID = 1L;

    private List<TaskLog> logs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group == null ? null : group.trim();
    }

    public Integer getCpuCoreRequired() {
        return cpuCoreRequired;
    }

    public void setCpuCoreRequired(Integer cpuCoreRequired) {
        this.cpuCoreRequired = cpuCoreRequired;
    }

    public Integer getRamRequired() {
        return ramRequired;
    }

    public void setRamRequired(Integer ramRequired) {
        this.ramRequired = ramRequired;
    }

    public Integer getBandwidthRequired() {
        return bandwidthRequired;
    }

    public void setBandwidthRequired(Integer bandwidthRequired) {
        this.bandwidthRequired = bandwidthRequired;
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

    public Boolean getInteractive() {
        return interactive;
    }

    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public JsonNode getParameter() {
        return parameter;
    }

    public void setParameter(JsonNode parameter) {
        this.parameter = parameter;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem == null ? null : operatingSystem.trim();
    }

    public JsonNode getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(JsonNode executionResult) {
        this.executionResult = executionResult;
    }

    public Date getExecutionStartAt() {
        return executionStartAt;
    }

    public void setExecutionStartAt(Date executionStartAt) {
        this.executionStartAt = executionStartAt;
    }

    public Date getExecutionEndAt() {
        return executionEndAt;
    }

    public void setExecutionEndAt(Date executionEndAt) {
        this.executionEndAt = executionEndAt;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Integer getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(Integer retryNumber) {
        this.retryNumber = retryNumber;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Boolean getSingleton() {
        return singleton;
    }

    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }

    public UUID getExternalIdentifier() {
        return externalIdentifier;
    }

    public void setExternalIdentifier(UUID externalIdentifier) {
        this.externalIdentifier = externalIdentifier;
    }

    public UUID getSingletonUuid() {
        return singletonUuid;
    }

    public void setSingletonUuid(UUID singletonUuid) {
        this.singletonUuid = singletonUuid;
    }

    public List<TaskLog> getLogs() {
        return logs;
    }

    public void setLogs(List<TaskLog> logs) {
        this.logs = logs;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (that == null) { return false; }
        if (getClass() != that.getClass()) { return false; }
        Task other = (Task) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
                && (this.getPriority() == null ? other.getPriority() == null
                        : this.getPriority().equals(other.getPriority()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getGroup() == null ? other.getGroup() == null : this.getGroup().equals(other.getGroup()))
                && (this.getCpuCoreRequired() == null ? other.getCpuCoreRequired() == null
                        : this.getCpuCoreRequired().equals(other.getCpuCoreRequired()))
                && (this.getRamRequired() == null ? other.getRamRequired() == null
                        : this.getRamRequired().equals(other.getRamRequired()))
                && (this.getBandwidthRequired() == null ? other.getBandwidthRequired() == null
                        : this.getBandwidthRequired().equals(other.getBandwidthRequired()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null
                        : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getInteractive() == null ? other.getInteractive() == null
                        : this.getInteractive().equals(other.getInteractive()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getParameter() == null ? other.getParameter() == null
                        : this.getParameter().equals(other.getParameter()))
                && (this.getTimeout() == null ? other.getTimeout() == null
                        : this.getTimeout().equals(other.getTimeout()))
                && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
                && (this.getFinished() == null ? other.getFinished() == null
                        : this.getFinished().equals(other.getFinished()))
                && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()))
                && (this.getOperatingSystem() == null ? other.getOperatingSystem() == null
                        : this.getOperatingSystem().equals(other.getOperatingSystem()))
                && (this.getExecutionResult() == null ? other.getExecutionResult() == null
                        : this.getExecutionResult().equals(other.getExecutionResult()))
                && (this.getExecutionStartAt() == null ? other.getExecutionStartAt() == null
                        : this.getExecutionStartAt().equals(other.getExecutionStartAt()))
                && (this.getExecutionEndAt() == null ? other.getExecutionEndAt() == null
                        : this.getExecutionEndAt().equals(other.getExecutionEndAt()))
                && (this.getMaxRetry() == null ? other.getMaxRetry() == null
                        : this.getMaxRetry().equals(other.getMaxRetry()))
                && (this.getRetryNumber() == null ? other.getRetryNumber() == null
                        : this.getRetryNumber().equals(other.getRetryNumber()))
                && (this.getWorkerId() == null ? other.getWorkerId() == null
                        : this.getWorkerId().equals(other.getWorkerId()))
                && (this.getSingleton() == null ? other.getSingleton() == null
                        : this.getSingleton().equals(other.getSingleton()))
                && (this.getExternalIdentifier() == null ? other.getExternalIdentifier() == null
                        : this.getExternalIdentifier().equals(other.getExternalIdentifier()))
                && (this.getSingletonUuid() == null ? other.getSingletonUuid() == null
                        : this.getSingletonUuid().equals(other.getSingletonUuid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        result = prime * result + ((getCpuCoreRequired() == null) ? 0 : getCpuCoreRequired().hashCode());
        result = prime * result + ((getRamRequired() == null) ? 0 : getRamRequired().hashCode());
        result = prime * result + ((getBandwidthRequired() == null) ? 0 : getBandwidthRequired().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getInteractive() == null) ? 0 : getInteractive().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getParameter() == null) ? 0 : getParameter().hashCode());
        result = prime * result + ((getTimeout() == null) ? 0 : getTimeout().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getFinished() == null) ? 0 : getFinished().hashCode());
        result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        result = prime * result + ((getOperatingSystem() == null) ? 0 : getOperatingSystem().hashCode());
        result = prime * result + ((getExecutionResult() == null) ? 0 : getExecutionResult().hashCode());
        result = prime * result + ((getExecutionStartAt() == null) ? 0 : getExecutionStartAt().hashCode());
        result = prime * result + ((getExecutionEndAt() == null) ? 0 : getExecutionEndAt().hashCode());
        result = prime * result + ((getMaxRetry() == null) ? 0 : getMaxRetry().hashCode());
        result = prime * result + ((getRetryNumber() == null) ? 0 : getRetryNumber().hashCode());
        result = prime * result + ((getWorkerId() == null) ? 0 : getWorkerId().hashCode());
        result = prime * result + ((getSingleton() == null) ? 0 : getSingleton().hashCode());
        result = prime * result + ((getExternalIdentifier() == null) ? 0 : getExternalIdentifier().hashCode());
        result = prime * result + ((getSingletonUuid() == null) ? 0 : getSingletonUuid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", uuid=").append(uuid);
        sb.append(", priority=").append(priority);
        sb.append(", type=").append(type);
        sb.append(", group=").append(group);
        sb.append(", cpuCoreRequired=").append(cpuCoreRequired);
        sb.append(", ramRequired=").append(ramRequired);
        sb.append(", bandwidthRequired=").append(bandwidthRequired);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", interactive=").append(interactive);
        sb.append(", status=").append(status);
        sb.append(", parameter=").append(parameter);
        sb.append(", timeout=").append(timeout);
        sb.append(", jobId=").append(jobId);
        sb.append(", finished=").append(finished);
        sb.append(", data=").append(data);
        sb.append(", operatingSystem=").append(operatingSystem);
        sb.append(", executionResult=").append(executionResult);
        sb.append(", executionStartAt=").append(executionStartAt);
        sb.append(", executionEndAt=").append(executionEndAt);
        sb.append(", maxRetry=").append(maxRetry);
        sb.append(", retryNumber=").append(retryNumber);
        sb.append(", workerId=").append(workerId);
        sb.append(", singleton=").append(singleton);
        sb.append(", externalIdentifier=").append(externalIdentifier);
        sb.append(", singletonUuid=").append(singletonUuid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public enum Column {
        id("id", "id", "BIGINT", false), name("name", "name", "VARCHAR", false), uuid("uuid", "uuid", "OTHER",
                false), priority("priority", "priority", "INTEGER", false), type("task_type_id", "type", "OTHER",
                        false), group("group_id", "group", "OTHER", false), cpuCoreRequired("cpu_core_required",
                                "cpuCoreRequired", "INTEGER",
                                false), ramRequired("ram_required", "ramRequired", "INTEGER", false), bandwidthRequired(
                                        "bandwidth_required", "bandwidthRequired", "INTEGER",
                                        false), createdAt("created_at", "createdAt", "TIMESTAMP", false), updatedAt(
                                                "updated_at", "updatedAt", "TIMESTAMP",
                                                false), interactive("interactive", "interactive", "BIT", false), status(
                                                        "status_id", "status", "OTHER", false), parameter("parameter",
                                                                "parameter", "OTHER", false), timeout("timeout",
                                                                        "timeout", "BIGINT", false), jobId("job_id",
                                                                                "jobId", "BIGINT", false), finished(
                                                                                        "is_finished", "finished",
                                                                                        "BIT", false), data("data",
                                                                                                "data", "OTHER",
                                                                                                false), operatingSystem(
                                                                                                        "operating_system_id",
                                                                                                        "operatingSystem",
                                                                                                        "OTHER",
                                                                                                        false), executionResult(
                                                                                                                "execution_result",
                                                                                                                "executionResult",
                                                                                                                "OTHER",
                                                                                                                false), executionStartAt(
                                                                                                                        "execution_start_at",
                                                                                                                        "executionStartAt",
                                                                                                                        "TIMESTAMP",
                                                                                                                        false), executionEndAt(
                                                                                                                                "execution_end_at",
                                                                                                                                "executionEndAt",
                                                                                                                                "TIMESTAMP",
                                                                                                                                false), maxRetry(
                                                                                                                                        "max_retry",
                                                                                                                                        "maxRetry",
                                                                                                                                        "INTEGER",
                                                                                                                                        false), retryNumber(
                                                                                                                                                "retry_number",
                                                                                                                                                "retryNumber",
                                                                                                                                                "INTEGER",
                                                                                                                                                false), workerId(
                                                                                                                                                        "worker_id",
                                                                                                                                                        "workerId",
                                                                                                                                                        "BIGINT",
                                                                                                                                                        false), singleton(
                                                                                                                                                                "singleton",
                                                                                                                                                                "singleton",
                                                                                                                                                                "BIT",
                                                                                                                                                                false), externalIdentifier(
                                                                                                                                                                        "external_identifier",
                                                                                                                                                                        "externalIdentifier",
                                                                                                                                                                        "OTHER",
                                                                                                                                                                        false), singletonUuid(
                                                                                                                                                                                "singleton_uuid",
                                                                                                                                                                                "singletonUuid",
                                                                                                                                                                                "OTHER",
                                                                                                                                                                                false);

        private static final String BEGINNING_DELIMITER = "\"";

        private static final String ENDING_DELIMITER = "\"";

        private final String column;

        private final boolean isColumnNameDelimited;

        private final String javaProperty;

        private final String jdbcType;

        public String value() {
            return this.column;
        }

        public String getValue() {
            return this.column;
        }

        public String getJavaProperty() {
            return this.javaProperty;
        }

        public String getJdbcType() {
            return this.jdbcType;
        }

        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        public static Column[] excludes(Column... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[] {});
        }

        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER)
                        .toString();
            }
            else {
                return this.column;
            }
        }

        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}
