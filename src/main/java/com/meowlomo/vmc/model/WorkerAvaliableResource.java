package com.meowlomo.vmc.model;

import java.io.Serializable;
import java.util.UUID;

public class WorkerAvaliableResource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2772614900026813981L;

    private Long id;
    private UUID uuid;
    private Integer cpuCore;
    private Integer bandwidth;
    private Integer ram;
    private Boolean interactive;
    private String operatingSystem;
    private String status;
    private String group;

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
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
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getCpuCore() {
        return cpuCore;
    }

    public void setCpuCore(Integer cpuCore) {
        this.cpuCore = cpuCore;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (that == null) { return false; }
        if (getClass() != that.getClass()) { return false; }
        WorkerAvaliableResource other = (WorkerAvaliableResource) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCpuCore() == null ? other.getCpuCore() == null
                        : this.getCpuCore().equals(other.getCpuCore()))
                && (this.getRam() == null ? other.getRam() == null : this.getRam().equals(other.getRam()))
                && (this.getBandwidth() == null ? other.getBandwidth() == null
                        : this.getBandwidth().equals(other.getBandwidth()))
                && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getInteractive() == null ? other.getInteractive() == null
                        : this.getInteractive().equals(other.getInteractive()))
                && (this.getOperatingSystem() == null ? other.getOperatingSystem() == null
                        : this.getOperatingSystem().equals(other.getOperatingSystem()))
                && (this.getGroup() == null ? other.getGroup() == null : this.getGroup().equals(other.getGroup()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCpuCore() == null) ? 0 : getCpuCore().hashCode());
        result = prime * result + ((getRam() == null) ? 0 : getRam().hashCode());
        result = prime * result + ((getBandwidth() == null) ? 0 : getBandwidth().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getInteractive() == null) ? 0 : getInteractive().hashCode());
        result = prime * result + ((getOperatingSystem() == null) ? 0 : getOperatingSystem().hashCode());
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", cpuCore=").append(cpuCore);
        sb.append(", ram=").append(ram);
        sb.append(", bandwidth=").append(bandwidth);
        sb.append(", uuid=").append(uuid);
        sb.append(", status=").append(status);
        sb.append(", interactive=").append(interactive);
        sb.append(", operatingSystem=").append(operatingSystem);
        sb.append(", group=").append(group);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
