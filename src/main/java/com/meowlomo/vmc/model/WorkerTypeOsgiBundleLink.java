package com.meowlomo.vmc.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class WorkerTypeOsgiBundleLink implements Serializable {
    private Long id;

    private String type;

    private String osgiBundleMD5;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getOsgiBundleMD5() {
        return osgiBundleMD5;
    }

    public void setOsgiBundleMD5(String osgiBundleMD5) {
        this.osgiBundleMD5 = osgiBundleMD5 == null ? null : osgiBundleMD5.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (that == null) { return false; }
        if (getClass() != that.getClass()) { return false; }
        WorkerTypeOsgiBundleLink other = (WorkerTypeOsgiBundleLink) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getOsgiBundleMD5() == null ? other.getOsgiBundleMD5() == null
                        : this.getOsgiBundleMD5().equals(other.getOsgiBundleMD5()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getOsgiBundleMD5() == null) ? 0 : getOsgiBundleMD5().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", osgiBundleMD5=").append(osgiBundleMD5);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}