package com.meowlomo.vmc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;

public class Worker implements Serializable {
    private Long id;

    private String architecture;

    private Integer cpuCore;

    private String hostname;

    private Integer bandwidth;

    private String ipAddress;

    private String name;

    private Integer ram;

    @ApiModelProperty(value = "UUID", dataType = "String")
    private UUID uuid;

    private String operatingSystem;

    private String status;

    private String group;

    private Boolean active;

    private String macAddress;

    private Date createdAt;

    private Date updatedAt;

    private Integer port;

    private String type;

    private String protocol;

    private Long taskId;

    private Boolean manageable;

    private UUID token;

    private static final long serialVersionUID = 1L;

    private Task task;

    private List<WorkerLog> logs;

    private List<WorkerVendor> vendors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture == null ? null : architecture.trim();
    }

    public Integer getCpuCore() {
        return cpuCore;
    }

    public void setCpuCore(Integer cpuCore) {
        this.cpuCore = cpuCore;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname == null ? null : hostname.trim();
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem == null ? null : operatingSystem.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group == null ? null : group.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress == null ? null : macAddress.trim();
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol == null ? null : protocol.trim();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Boolean getManageable() {
        return manageable;
    }

    public void setManageable(Boolean manageable) {
        this.manageable = manageable;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<WorkerLog> getLogs() {
        return logs;
    }

    public void setLogs(List<WorkerLog> logs) {
        this.logs = logs;
    }

    public List<WorkerVendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<WorkerVendor> vendors) {
        this.vendors = vendors;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (that == null) { return false; }
        if (getClass() != that.getClass()) { return false; }
        Worker other = (Worker) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getArchitecture() == null ? other.getArchitecture() == null
                        : this.getArchitecture().equals(other.getArchitecture()))
                && (this.getCpuCore() == null ? other.getCpuCore() == null
                        : this.getCpuCore().equals(other.getCpuCore()))
                && (this.getHostname() == null ? other.getHostname() == null
                        : this.getHostname().equals(other.getHostname()))
                && (this.getBandwidth() == null ? other.getBandwidth() == null
                        : this.getBandwidth().equals(other.getBandwidth()))
                && (this.getIpAddress() == null ? other.getIpAddress() == null
                        : this.getIpAddress().equals(other.getIpAddress()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getRam() == null ? other.getRam() == null : this.getRam().equals(other.getRam()))
                && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
                && (this.getOperatingSystem() == null ? other.getOperatingSystem() == null
                        : this.getOperatingSystem().equals(other.getOperatingSystem()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getGroup() == null ? other.getGroup() == null : this.getGroup().equals(other.getGroup()))
                && (this.getActive() == null ? other.getActive() == null : this.getActive().equals(other.getActive()))
                && (this.getMacAddress() == null ? other.getMacAddress() == null
                        : this.getMacAddress().equals(other.getMacAddress()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null
                        : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getPort() == null ? other.getPort() == null : this.getPort().equals(other.getPort()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getProtocol() == null ? other.getProtocol() == null
                        : this.getProtocol().equals(other.getProtocol()))
                && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
                && (this.getManageable() == null ? other.getManageable() == null
                        : this.getManageable().equals(other.getManageable()))
                && (this.getToken() == null ? other.getToken() == null : this.getToken().equals(other.getToken()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getArchitecture() == null) ? 0 : getArchitecture().hashCode());
        result = prime * result + ((getCpuCore() == null) ? 0 : getCpuCore().hashCode());
        result = prime * result + ((getHostname() == null) ? 0 : getHostname().hashCode());
        result = prime * result + ((getBandwidth() == null) ? 0 : getBandwidth().hashCode());
        result = prime * result + ((getIpAddress() == null) ? 0 : getIpAddress().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getRam() == null) ? 0 : getRam().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getOperatingSystem() == null) ? 0 : getOperatingSystem().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        result = prime * result + ((getActive() == null) ? 0 : getActive().hashCode());
        result = prime * result + ((getMacAddress() == null) ? 0 : getMacAddress().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getPort() == null) ? 0 : getPort().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getProtocol() == null) ? 0 : getProtocol().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getManageable() == null) ? 0 : getManageable().hashCode());
        result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", architecture=").append(architecture);
        sb.append(", cpuCore=").append(cpuCore);
        sb.append(", hostname=").append(hostname);
        sb.append(", bandwidth=").append(bandwidth);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", name=").append(name);
        sb.append(", ram=").append(ram);
        sb.append(", uuid=").append(uuid);
        sb.append(", operatingSystem=").append(operatingSystem);
        sb.append(", status=").append(status);
        sb.append(", group=").append(group);
        sb.append(", active=").append(active);
        sb.append(", macAddress=").append(macAddress);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", port=").append(port);
        sb.append(", type=").append(type);
        sb.append(", protocol=").append(protocol);
        sb.append(", taskId=").append(taskId);
        sb.append(", manageable=").append(manageable);
        sb.append(", token=").append(token);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public enum Column {
        id("id", "id", "BIGINT", false), architecture("architecture", "architecture", "VARCHAR", false), cpuCore(
                "cpu_core", "cpuCore", "INTEGER",
                false), hostname("hostname", "hostname", "VARCHAR", false), bandwidth("bandwidth", "bandwidth",
                        "INTEGER", false), ipAddress("ip_address", "ipAddress", "VARCHAR", false), name("name", "name",
                                "VARCHAR", false), ram("ram", "ram", "INTEGER", false), uuid("uuid", "uuid", "OTHER",
                                        false), operatingSystem("operating_system_id", "operatingSystem", "OTHER",
                                                false), status("status_id", "status", "OTHER", false), group("group_id",
                                                        "group", "OTHER",
                                                        false), active("is_active", "active", "BIT", false), macAddress(
                                                                "mac_address", "macAddress", "VARCHAR",
                                                                false), createdAt("created_at", "createdAt",
                                                                        "TIMESTAMP", false), updatedAt("updated_at",
                                                                                "updatedAt", "TIMESTAMP",
                                                                                false), port("port", "port", "INTEGER",
                                                                                        false), type("worker_type_id",
                                                                                                "type", "OTHER",
                                                                                                false), protocol(
                                                                                                        "protocol",
                                                                                                        "protocol",
                                                                                                        "VARCHAR",
                                                                                                        false), taskId(
                                                                                                                "task_id",
                                                                                                                "taskId",
                                                                                                                "BIGINT",
                                                                                                                false), manageable(
                                                                                                                        "manageable",
                                                                                                                        "manageable",
                                                                                                                        "BIT",
                                                                                                                        false), token(
                                                                                                                                "token",
                                                                                                                                "token",
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