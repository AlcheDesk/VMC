package com.meowlomo.vmc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkerAvaliableResourceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WorkerAvaliableResourceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {

        protected List<Criterion> uuidCriteria;

        protected List<Criterion> groupCriteria;

        protected List<Criterion> statusCriteria;

        protected List<Criterion> operatingSystemCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            groupCriteria = new ArrayList<Criterion>();
            uuidCriteria = new ArrayList<Criterion>();
            statusCriteria = new ArrayList<Criterion>();
            operatingSystemCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getUuidCriteria() {
            return uuidCriteria;
        }

        protected void addUuidCriterion(String condition, Object value, String property) {
            if (value == null) { throw new RuntimeException("Value for " + property + " cannot be null"); }
            uuidCriteria.add(new Criterion(condition, value, "com.meowlomo.ems.typehandler.UUIDTypeHandler"));
            allCriteria = null;
        }

        protected void addUuidCriterion(String condition, UUID value1, UUID value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            uuidCriteria.add(new Criterion(condition, value1, value2, "com.meowlomo.ems.typehandler.UUIDTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getGroupCriteria() {
            return groupCriteria;
        }

        protected void addGroupCriterion(String condition, Object value, String property) {
            if (value == null) { throw new RuntimeException("Value for " + property + " cannot be null"); }
            groupCriteria.add(new Criterion(condition, value, "com.meowlomo.ems.typehandler.GroupTypeHandler"));
            allCriteria = null;
        }

        protected void addGroupCriterion(String condition, String value1, String value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            groupCriteria
                    .add(new Criterion(condition, value1, value2, "com.meowlomo.ems.typehandler.GroupTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getStatusCriteria() {
            return statusCriteria;
        }

        protected void addStatusCriterion(String condition, Object value, String property) {
            if (value == null) { throw new RuntimeException("Value for " + property + " cannot be null"); }
            statusCriteria.add(new Criterion(condition, value, "com.meowlomo.ems.typehandler.StatusTypeHandler"));
            allCriteria = null;
        }

        protected void addStatusCriterion(String condition, String value1, String value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            statusCriteria
                    .add(new Criterion(condition, value1, value2, "com.meowlomo.ems.typehandler.StatusTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getOperatingSystemCriteria() {
            return operatingSystemCriteria;
        }

        protected void addOperatingSystemCriterion(String condition, Object value, String property) {
            if (value == null) { throw new RuntimeException("Value for " + property + " cannot be null"); }
            operatingSystemCriteria
                    .add(new Criterion(condition, value, "com.meowlomo.ems.typehandler.OperatingSystemTypeHandler"));
            allCriteria = null;
        }

        protected void addOperatingSystemCriterion(String condition, String value1, String value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            operatingSystemCriteria.add(new Criterion(condition, value1, value2,
                    "com.meowlomo.ems.typehandler.OperatingSystemTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0 || uuidCriteria.size() > 0 || groupCriteria.size() > 0
                    || statusCriteria.size() > 0 || operatingSystemCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(uuidCriteria);
                allCriteria.addAll(groupCriteria);
                allCriteria.addAll(statusCriteria);
                allCriteria.addAll(operatingSystemCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) { throw new RuntimeException("Value for condition cannot be null"); }
            criteria.add(new Criterion(condition));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) { throw new RuntimeException("Value for " + property + " cannot be null"); }
            criteria.add(new Criterion(condition, value));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
            allCriteria = null;
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUuidIsNull() {
            addCriterion("uuid is null");
            return (Criteria) this;
        }

        public Criteria andUuidIsNotNull() {
            addCriterion("uuid is not null");
            return (Criteria) this;
        }

        public Criteria andUuidEqualTo(UUID value) {
            addUuidCriterion("uuid =", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotEqualTo(UUID value) {
            addUuidCriterion("uuid <>", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThan(UUID value) {
            addUuidCriterion("uuid >", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThanOrEqualTo(UUID value) {
            addUuidCriterion("uuid >=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThan(UUID value) {
            addUuidCriterion("uuid <", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThanOrEqualTo(UUID value) {
            addUuidCriterion("uuid <=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidIn(List<UUID> values) {
            addUuidCriterion("uuid in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotIn(List<UUID> values) {
            addUuidCriterion("uuid not in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidBetween(UUID value1, UUID value2) {
            addUuidCriterion("uuid between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotBetween(UUID value1, UUID value2) {
            addUuidCriterion("uuid not between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andGroupIsNull() {
            addCriterion("group_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupIsNotNull() {
            addCriterion("group_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupEqualTo(String value) {
            addGroupCriterion("group_id =", value, "group");
            return (Criteria) this;
        }

        public Criteria andGroupNotEqualTo(String value) {
            addGroupCriterion("group_id <>", value, "group");
            return (Criteria) this;
        }

        public Criteria andGroupGreaterThan(String value) {
            addGroupCriterion("group_id >", value, "group");
            return (Criteria) this;
        }

        public Criteria andGroupGreaterThanOrEqualTo(String value) {
            addGroupCriterion("group_id >=", value, "group");
            return (Criteria) this;
        }

        public Criteria andGroupLessThan(String value) {
            addGroupCriterion("group_id <", value, "group");
            return (Criteria) this;
        }

        public Criteria andGroupLessThanOrEqualTo(String value) {
            addGroupCriterion("group_id <=", value, "group");
            return (Criteria) this;
        }

        public Criteria andGroupIn(List<String> values) {
            addGroupCriterion("group_id in", values, "group");
            return (Criteria) this;
        }

        public Criteria andGroupNotIn(List<String> values) {
            addGroupCriterion("group_id not in", values, "group");
            return (Criteria) this;
        }

        public Criteria andGroupBetween(String value1, String value2) {
            addGroupCriterion("group_id between", value1, value2, "group");
            return (Criteria) this;
        }

        public Criteria andGroupNotBetween(String value1, String value2) {
            addGroupCriterion("group_id not between", value1, value2, "group");
            return (Criteria) this;
        }

        public Criteria andCpuCoreIsNull() {
            addCriterion("cpu_core is null");
            return (Criteria) this;
        }

        public Criteria andCpuCoreIsNotNull() {
            addCriterion("cpu_core is not null");
            return (Criteria) this;
        }

        public Criteria andCpuCoreEqualTo(Integer value) {
            addCriterion("cpu_core =", value, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreNotEqualTo(Integer value) {
            addCriterion("cpu_core <>", value, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreGreaterThan(Integer value) {
            addCriterion("cpu_core >", value, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_core >=", value, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreLessThan(Integer value) {
            addCriterion("cpu_core <", value, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_core <=", value, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreIn(List<Integer> values) {
            addCriterion("cpu_core in", values, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreNotIn(List<Integer> values) {
            addCriterion("cpu_core not in", values, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreBetween(Integer value1, Integer value2) {
            addCriterion("cpu_core between", value1, value2, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andCpuCoreNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_core not between", value1, value2, "cpuCore");
            return (Criteria) this;
        }

        public Criteria andRamIsNull() {
            addCriterion("ram is null");
            return (Criteria) this;
        }

        public Criteria andRamIsNotNull() {
            addCriterion("ram is not null");
            return (Criteria) this;
        }

        public Criteria andRamEqualTo(Integer value) {
            addCriterion("ram =", value, "ram");
            return (Criteria) this;
        }

        public Criteria andRamNotEqualTo(Integer value) {
            addCriterion("ram <>", value, "ram");
            return (Criteria) this;
        }

        public Criteria andRamGreaterThan(Integer value) {
            addCriterion("ram >", value, "ram");
            return (Criteria) this;
        }

        public Criteria andRamGreaterThanOrEqualTo(Integer value) {
            addCriterion("ram >=", value, "ram");
            return (Criteria) this;
        }

        public Criteria andRamLessThan(Integer value) {
            addCriterion("ram <", value, "ram");
            return (Criteria) this;
        }

        public Criteria andRamLessThanOrEqualTo(Integer value) {
            addCriterion("ram <=", value, "ram");
            return (Criteria) this;
        }

        public Criteria andRamIn(List<Integer> values) {
            addCriterion("ram in", values, "ram");
            return (Criteria) this;
        }

        public Criteria andRamNotIn(List<Integer> values) {
            addCriterion("ram not in", values, "ram");
            return (Criteria) this;
        }

        public Criteria andRamBetween(Integer value1, Integer value2) {
            addCriterion("ram between", value1, value2, "ram");
            return (Criteria) this;
        }

        public Criteria andRamNotBetween(Integer value1, Integer value2) {
            addCriterion("ram not between", value1, value2, "ram");
            return (Criteria) this;
        }

        public Criteria andBandwidthIsNull() {
            addCriterion("bandwidth is null");
            return (Criteria) this;
        }

        public Criteria andBandwidthIsNotNull() {
            addCriterion("bandwidth is not null");
            return (Criteria) this;
        }

        public Criteria andBandwidthEqualTo(Integer value) {
            addCriterion("bandwidth =", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotEqualTo(Integer value) {
            addCriterion("bandwidth <>", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthGreaterThan(Integer value) {
            addCriterion("bandwidth >", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthGreaterThanOrEqualTo(Integer value) {
            addCriterion("bandwidth >=", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLessThan(Integer value) {
            addCriterion("bandwidth <", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLessThanOrEqualTo(Integer value) {
            addCriterion("bandwidth <=", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthIn(List<Integer> values) {
            addCriterion("bandwidth in", values, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotIn(List<Integer> values) {
            addCriterion("bandwidth not in", values, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthBetween(Integer value1, Integer value2) {
            addCriterion("bandwidth between", value1, value2, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotBetween(Integer value1, Integer value2) {
            addCriterion("bandwidth not between", value1, value2, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andInteractiveIsNull() {
            addCriterion("interactive is null");
            return (Criteria) this;
        }

        public Criteria andInteractiveIsNotNull() {
            addCriterion("interactive is not null");
            return (Criteria) this;
        }

        public Criteria andInteractiveEqualTo(Boolean value) {
            addCriterion("interactive =", value, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveNotEqualTo(Boolean value) {
            addCriterion("interactive <>", value, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveGreaterThan(Boolean value) {
            addCriterion("interactive >", value, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveGreaterThanOrEqualTo(Boolean value) {
            addCriterion("interactive >=", value, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveLessThan(Boolean value) {
            addCriterion("interactive <", value, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveLessThanOrEqualTo(Boolean value) {
            addCriterion("interactive <=", value, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveIn(List<Boolean> values) {
            addCriterion("interactive in", values, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveNotIn(List<Boolean> values) {
            addCriterion("interactive not in", values, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveBetween(Boolean value1, Boolean value2) {
            addCriterion("interactive between", value1, value2, "interactive");
            return (Criteria) this;
        }

        public Criteria andInteractiveNotBetween(Boolean value1, Boolean value2) {
            addCriterion("interactive not between", value1, value2, "interactive");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status_id is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status_id is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addStatusCriterion("status_id =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addStatusCriterion("status_id <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addStatusCriterion("status_id >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addStatusCriterion("status_id >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addStatusCriterion("status_id <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addStatusCriterion("status_id <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addStatusCriterion("status_id in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addStatusCriterion("status_id not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addStatusCriterion("status_id between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addStatusCriterion("status_id not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemIsNull() {
            addCriterion("operating_system_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemIsNotNull() {
            addCriterion("operating_system_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemEqualTo(String value) {
            addOperatingSystemCriterion("operating_system_id =", value, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemNotEqualTo(String value) {
            addOperatingSystemCriterion("operating_system_id <>", value, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemGreaterThan(String value) {
            addOperatingSystemCriterion("operating_system_id >", value, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemGreaterThanOrEqualTo(String value) {
            addOperatingSystemCriterion("operating_system_id >=", value, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemLessThan(String value) {
            addOperatingSystemCriterion("operating_system_id <", value, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemLessThanOrEqualTo(String value) {
            addOperatingSystemCriterion("operating_system_id <=", value, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemIn(List<String> values) {
            addOperatingSystemCriterion("operating_system_id in", values, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemNotIn(List<String> values) {
            addOperatingSystemCriterion("operating_system_id not in", values, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemBetween(String value1, String value2) {
            addOperatingSystemCriterion("operating_system_id between", value1, value2, "operatingSystem");
            return (Criteria) this;
        }

        public Criteria andOperatingSystemNotBetween(String value1, String value2) {
            addOperatingSystemCriterion("operating_system_id not between", value1, value2, "operatingSystem");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            }
            else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}