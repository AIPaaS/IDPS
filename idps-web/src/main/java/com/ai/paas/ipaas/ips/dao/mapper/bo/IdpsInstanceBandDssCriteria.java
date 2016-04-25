package com.ai.paas.ipaas.ips.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class IdpsInstanceBandDssCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public IdpsInstanceBandDssCriteria() {
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

    public void setLimitStart(Integer limitStart) {
        this.limitStart=limitStart;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd=limitEnd;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNull() {
            addCriterion("service_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNotNull() {
            addCriterion("service_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceIdEqualTo(String value) {
            addCriterion("service_id =", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotEqualTo(String value) {
            addCriterion("service_id <>", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThan(String value) {
            addCriterion("service_id >", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("service_id >=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThan(String value) {
            addCriterion("service_id <", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThanOrEqualTo(String value) {
            addCriterion("service_id <=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLike(String value) {
            addCriterion("service_id like", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotLike(String value) {
            addCriterion("service_id not like", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdIn(List<String> values) {
            addCriterion("service_id in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotIn(List<String> values) {
            addCriterion("service_id not in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdBetween(String value1, String value2) {
            addCriterion("service_id between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotBetween(String value1, String value2) {
            addCriterion("service_id not between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdIsNull() {
            addCriterion("dss_service_id is null");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdIsNotNull() {
            addCriterion("dss_service_id is not null");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdEqualTo(String value) {
            addCriterion("dss_service_id =", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdNotEqualTo(String value) {
            addCriterion("dss_service_id <>", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdGreaterThan(String value) {
            addCriterion("dss_service_id >", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("dss_service_id >=", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdLessThan(String value) {
            addCriterion("dss_service_id <", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdLessThanOrEqualTo(String value) {
            addCriterion("dss_service_id <=", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdLike(String value) {
            addCriterion("dss_service_id like", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdNotLike(String value) {
            addCriterion("dss_service_id not like", value, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdIn(List<String> values) {
            addCriterion("dss_service_id in", values, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdNotIn(List<String> values) {
            addCriterion("dss_service_id not in", values, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdBetween(String value1, String value2) {
            addCriterion("dss_service_id between", value1, value2, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssServiceIdNotBetween(String value1, String value2) {
            addCriterion("dss_service_id not between", value1, value2, "dssServiceId");
            return (Criteria) this;
        }

        public Criteria andDssPidIsNull() {
            addCriterion("dss_pid is null");
            return (Criteria) this;
        }

        public Criteria andDssPidIsNotNull() {
            addCriterion("dss_pid is not null");
            return (Criteria) this;
        }

        public Criteria andDssPidEqualTo(String value) {
            addCriterion("dss_pid =", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidNotEqualTo(String value) {
            addCriterion("dss_pid <>", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidGreaterThan(String value) {
            addCriterion("dss_pid >", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidGreaterThanOrEqualTo(String value) {
            addCriterion("dss_pid >=", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidLessThan(String value) {
            addCriterion("dss_pid <", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidLessThanOrEqualTo(String value) {
            addCriterion("dss_pid <=", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidLike(String value) {
            addCriterion("dss_pid like", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidNotLike(String value) {
            addCriterion("dss_pid not like", value, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidIn(List<String> values) {
            addCriterion("dss_pid in", values, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidNotIn(List<String> values) {
            addCriterion("dss_pid not in", values, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidBetween(String value1, String value2) {
            addCriterion("dss_pid between", value1, value2, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssPidNotBetween(String value1, String value2) {
            addCriterion("dss_pid not between", value1, value2, "dssPid");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdIsNull() {
            addCriterion("dss_service_pwd is null");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdIsNotNull() {
            addCriterion("dss_service_pwd is not null");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdEqualTo(String value) {
            addCriterion("dss_service_pwd =", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdNotEqualTo(String value) {
            addCriterion("dss_service_pwd <>", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdGreaterThan(String value) {
            addCriterion("dss_service_pwd >", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdGreaterThanOrEqualTo(String value) {
            addCriterion("dss_service_pwd >=", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdLessThan(String value) {
            addCriterion("dss_service_pwd <", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdLessThanOrEqualTo(String value) {
            addCriterion("dss_service_pwd <=", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdLike(String value) {
            addCriterion("dss_service_pwd like", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdNotLike(String value) {
            addCriterion("dss_service_pwd not like", value, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdIn(List<String> values) {
            addCriterion("dss_service_pwd in", values, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdNotIn(List<String> values) {
            addCriterion("dss_service_pwd not in", values, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdBetween(String value1, String value2) {
            addCriterion("dss_service_pwd between", value1, value2, "dssServicePwd");
            return (Criteria) this;
        }

        public Criteria andDssServicePwdNotBetween(String value1, String value2) {
            addCriterion("dss_service_pwd not between", value1, value2, "dssServicePwd");
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
            } else {
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