package com.ai.paas.ipaas.ips.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class IdpsConfigInfoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public IdpsConfigInfoCriteria() {
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

        public Criteria andGmModeIsNull() {
            addCriterion("gm_mode is null");
            return (Criteria) this;
        }

        public Criteria andGmModeIsNotNull() {
            addCriterion("gm_mode is not null");
            return (Criteria) this;
        }

        public Criteria andGmModeEqualTo(String value) {
            addCriterion("gm_mode =", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeNotEqualTo(String value) {
            addCriterion("gm_mode <>", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeGreaterThan(String value) {
            addCriterion("gm_mode >", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeGreaterThanOrEqualTo(String value) {
            addCriterion("gm_mode >=", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeLessThan(String value) {
            addCriterion("gm_mode <", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeLessThanOrEqualTo(String value) {
            addCriterion("gm_mode <=", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeLike(String value) {
            addCriterion("gm_mode like", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeNotLike(String value) {
            addCriterion("gm_mode not like", value, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeIn(List<String> values) {
            addCriterion("gm_mode in", values, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeNotIn(List<String> values) {
            addCriterion("gm_mode not in", values, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeBetween(String value1, String value2) {
            addCriterion("gm_mode between", value1, value2, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmModeNotBetween(String value1, String value2) {
            addCriterion("gm_mode not between", value1, value2, "gmMode");
            return (Criteria) this;
        }

        public Criteria andGmPathIsNull() {
            addCriterion("gm_path is null");
            return (Criteria) this;
        }

        public Criteria andGmPathIsNotNull() {
            addCriterion("gm_path is not null");
            return (Criteria) this;
        }

        public Criteria andGmPathEqualTo(String value) {
            addCriterion("gm_path =", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathNotEqualTo(String value) {
            addCriterion("gm_path <>", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathGreaterThan(String value) {
            addCriterion("gm_path >", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathGreaterThanOrEqualTo(String value) {
            addCriterion("gm_path >=", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathLessThan(String value) {
            addCriterion("gm_path <", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathLessThanOrEqualTo(String value) {
            addCriterion("gm_path <=", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathLike(String value) {
            addCriterion("gm_path like", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathNotLike(String value) {
            addCriterion("gm_path not like", value, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathIn(List<String> values) {
            addCriterion("gm_path in", values, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathNotIn(List<String> values) {
            addCriterion("gm_path not in", values, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathBetween(String value1, String value2) {
            addCriterion("gm_path between", value1, value2, "gmPath");
            return (Criteria) this;
        }

        public Criteria andGmPathNotBetween(String value1, String value2) {
            addCriterion("gm_path not between", value1, value2, "gmPath");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitIsNull() {
            addCriterion("image_name_split is null");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitIsNotNull() {
            addCriterion("image_name_split is not null");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitEqualTo(String value) {
            addCriterion("image_name_split =", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitNotEqualTo(String value) {
            addCriterion("image_name_split <>", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitGreaterThan(String value) {
            addCriterion("image_name_split >", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitGreaterThanOrEqualTo(String value) {
            addCriterion("image_name_split >=", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitLessThan(String value) {
            addCriterion("image_name_split <", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitLessThanOrEqualTo(String value) {
            addCriterion("image_name_split <=", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitLike(String value) {
            addCriterion("image_name_split like", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitNotLike(String value) {
            addCriterion("image_name_split not like", value, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitIn(List<String> values) {
            addCriterion("image_name_split in", values, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitNotIn(List<String> values) {
            addCriterion("image_name_split not in", values, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitBetween(String value1, String value2) {
            addCriterion("image_name_split between", value1, value2, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageNameSplitNotBetween(String value1, String value2) {
            addCriterion("image_name_split not between", value1, value2, "imageNameSplit");
            return (Criteria) this;
        }

        public Criteria andImageTypeIsNull() {
            addCriterion("image_type is null");
            return (Criteria) this;
        }

        public Criteria andImageTypeIsNotNull() {
            addCriterion("image_type is not null");
            return (Criteria) this;
        }

        public Criteria andImageTypeEqualTo(String value) {
            addCriterion("image_type =", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeNotEqualTo(String value) {
            addCriterion("image_type <>", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeGreaterThan(String value) {
            addCriterion("image_type >", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeGreaterThanOrEqualTo(String value) {
            addCriterion("image_type >=", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeLessThan(String value) {
            addCriterion("image_type <", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeLessThanOrEqualTo(String value) {
            addCriterion("image_type <=", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeLike(String value) {
            addCriterion("image_type like", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeNotLike(String value) {
            addCriterion("image_type not like", value, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeIn(List<String> values) {
            addCriterion("image_type in", values, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeNotIn(List<String> values) {
            addCriterion("image_type not in", values, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeBetween(String value1, String value2) {
            addCriterion("image_type between", value1, value2, "imageType");
            return (Criteria) this;
        }

        public Criteria andImageTypeNotBetween(String value1, String value2) {
            addCriterion("image_type not between", value1, value2, "imageType");
            return (Criteria) this;
        }

        public Criteria andMaxActiveIsNull() {
            addCriterion("max_active is null");
            return (Criteria) this;
        }

        public Criteria andMaxActiveIsNotNull() {
            addCriterion("max_active is not null");
            return (Criteria) this;
        }

        public Criteria andMaxActiveEqualTo(String value) {
            addCriterion("max_active =", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveNotEqualTo(String value) {
            addCriterion("max_active <>", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveGreaterThan(String value) {
            addCriterion("max_active >", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveGreaterThanOrEqualTo(String value) {
            addCriterion("max_active >=", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveLessThan(String value) {
            addCriterion("max_active <", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveLessThanOrEqualTo(String value) {
            addCriterion("max_active <=", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveLike(String value) {
            addCriterion("max_active like", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveNotLike(String value) {
            addCriterion("max_active not like", value, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveIn(List<String> values) {
            addCriterion("max_active in", values, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveNotIn(List<String> values) {
            addCriterion("max_active not in", values, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveBetween(String value1, String value2) {
            addCriterion("max_active between", value1, value2, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxActiveNotBetween(String value1, String value2) {
            addCriterion("max_active not between", value1, value2, "maxActive");
            return (Criteria) this;
        }

        public Criteria andMaxIdleIsNull() {
            addCriterion("max_idle is null");
            return (Criteria) this;
        }

        public Criteria andMaxIdleIsNotNull() {
            addCriterion("max_idle is not null");
            return (Criteria) this;
        }

        public Criteria andMaxIdleEqualTo(String value) {
            addCriterion("max_idle =", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleNotEqualTo(String value) {
            addCriterion("max_idle <>", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleGreaterThan(String value) {
            addCriterion("max_idle >", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleGreaterThanOrEqualTo(String value) {
            addCriterion("max_idle >=", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleLessThan(String value) {
            addCriterion("max_idle <", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleLessThanOrEqualTo(String value) {
            addCriterion("max_idle <=", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleLike(String value) {
            addCriterion("max_idle like", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleNotLike(String value) {
            addCriterion("max_idle not like", value, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleIn(List<String> values) {
            addCriterion("max_idle in", values, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleNotIn(List<String> values) {
            addCriterion("max_idle not in", values, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleBetween(String value1, String value2) {
            addCriterion("max_idle between", value1, value2, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxIdleNotBetween(String value1, String value2) {
            addCriterion("max_idle not between", value1, value2, "maxIdle");
            return (Criteria) this;
        }

        public Criteria andMaxWaitIsNull() {
            addCriterion("max_wait is null");
            return (Criteria) this;
        }

        public Criteria andMaxWaitIsNotNull() {
            addCriterion("max_wait is not null");
            return (Criteria) this;
        }

        public Criteria andMaxWaitEqualTo(String value) {
            addCriterion("max_wait =", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitNotEqualTo(String value) {
            addCriterion("max_wait <>", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitGreaterThan(String value) {
            addCriterion("max_wait >", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitGreaterThanOrEqualTo(String value) {
            addCriterion("max_wait >=", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitLessThan(String value) {
            addCriterion("max_wait <", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitLessThanOrEqualTo(String value) {
            addCriterion("max_wait <=", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitLike(String value) {
            addCriterion("max_wait like", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitNotLike(String value) {
            addCriterion("max_wait not like", value, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitIn(List<String> values) {
            addCriterion("max_wait in", values, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitNotIn(List<String> values) {
            addCriterion("max_wait not in", values, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitBetween(String value1, String value2) {
            addCriterion("max_wait between", value1, value2, "maxWait");
            return (Criteria) this;
        }

        public Criteria andMaxWaitNotBetween(String value1, String value2) {
            addCriterion("max_wait not between", value1, value2, "maxWait");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowIsNull() {
            addCriterion("test_on_borrow is null");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowIsNotNull() {
            addCriterion("test_on_borrow is not null");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowEqualTo(String value) {
            addCriterion("test_on_borrow =", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowNotEqualTo(String value) {
            addCriterion("test_on_borrow <>", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowGreaterThan(String value) {
            addCriterion("test_on_borrow >", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowGreaterThanOrEqualTo(String value) {
            addCriterion("test_on_borrow >=", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowLessThan(String value) {
            addCriterion("test_on_borrow <", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowLessThanOrEqualTo(String value) {
            addCriterion("test_on_borrow <=", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowLike(String value) {
            addCriterion("test_on_borrow like", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowNotLike(String value) {
            addCriterion("test_on_borrow not like", value, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowIn(List<String> values) {
            addCriterion("test_on_borrow in", values, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowNotIn(List<String> values) {
            addCriterion("test_on_borrow not in", values, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowBetween(String value1, String value2) {
            addCriterion("test_on_borrow between", value1, value2, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnBorrowNotBetween(String value1, String value2) {
            addCriterion("test_on_borrow not between", value1, value2, "testOnBorrow");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnIsNull() {
            addCriterion("test_on_return is null");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnIsNotNull() {
            addCriterion("test_on_return is not null");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnEqualTo(String value) {
            addCriterion("test_on_return =", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnNotEqualTo(String value) {
            addCriterion("test_on_return <>", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnGreaterThan(String value) {
            addCriterion("test_on_return >", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnGreaterThanOrEqualTo(String value) {
            addCriterion("test_on_return >=", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnLessThan(String value) {
            addCriterion("test_on_return <", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnLessThanOrEqualTo(String value) {
            addCriterion("test_on_return <=", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnLike(String value) {
            addCriterion("test_on_return like", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnNotLike(String value) {
            addCriterion("test_on_return not like", value, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnIn(List<String> values) {
            addCriterion("test_on_return in", values, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnNotIn(List<String> values) {
            addCriterion("test_on_return not in", values, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnBetween(String value1, String value2) {
            addCriterion("test_on_return between", value1, value2, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andTestOnReturnNotBetween(String value1, String value2) {
            addCriterion("test_on_return not between", value1, value2, "testOnReturn");
            return (Criteria) this;
        }

        public Criteria andExtentIsNull() {
            addCriterion("extent is null");
            return (Criteria) this;
        }

        public Criteria andExtentIsNotNull() {
            addCriterion("extent is not null");
            return (Criteria) this;
        }

        public Criteria andExtentEqualTo(String value) {
            addCriterion("extent =", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentNotEqualTo(String value) {
            addCriterion("extent <>", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentGreaterThan(String value) {
            addCriterion("extent >", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentGreaterThanOrEqualTo(String value) {
            addCriterion("extent >=", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentLessThan(String value) {
            addCriterion("extent <", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentLessThanOrEqualTo(String value) {
            addCriterion("extent <=", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentLike(String value) {
            addCriterion("extent like", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentNotLike(String value) {
            addCriterion("extent not like", value, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentIn(List<String> values) {
            addCriterion("extent in", values, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentNotIn(List<String> values) {
            addCriterion("extent not in", values, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentBetween(String value1, String value2) {
            addCriterion("extent between", value1, value2, "extent");
            return (Criteria) this;
        }

        public Criteria andExtentNotBetween(String value1, String value2) {
            addCriterion("extent not between", value1, value2, "extent");
            return (Criteria) this;
        }

        public Criteria andQualityIsNull() {
            addCriterion("quality is null");
            return (Criteria) this;
        }

        public Criteria andQualityIsNotNull() {
            addCriterion("quality is not null");
            return (Criteria) this;
        }

        public Criteria andQualityEqualTo(String value) {
            addCriterion("quality =", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotEqualTo(String value) {
            addCriterion("quality <>", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityGreaterThan(String value) {
            addCriterion("quality >", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityGreaterThanOrEqualTo(String value) {
            addCriterion("quality >=", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityLessThan(String value) {
            addCriterion("quality <", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityLessThanOrEqualTo(String value) {
            addCriterion("quality <=", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityLike(String value) {
            addCriterion("quality like", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotLike(String value) {
            addCriterion("quality not like", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityIn(List<String> values) {
            addCriterion("quality in", values, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotIn(List<String> values) {
            addCriterion("quality not in", values, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityBetween(String value1, String value2) {
            addCriterion("quality between", value1, value2, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotBetween(String value1, String value2) {
            addCriterion("quality not between", value1, value2, "quality");
            return (Criteria) this;
        }

        public Criteria andCommandSizeIsNull() {
            addCriterion("command_size is null");
            return (Criteria) this;
        }

        public Criteria andCommandSizeIsNotNull() {
            addCriterion("command_size is not null");
            return (Criteria) this;
        }

        public Criteria andCommandSizeEqualTo(String value) {
            addCriterion("command_size =", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeNotEqualTo(String value) {
            addCriterion("command_size <>", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeGreaterThan(String value) {
            addCriterion("command_size >", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeGreaterThanOrEqualTo(String value) {
            addCriterion("command_size >=", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeLessThan(String value) {
            addCriterion("command_size <", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeLessThanOrEqualTo(String value) {
            addCriterion("command_size <=", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeLike(String value) {
            addCriterion("command_size like", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeNotLike(String value) {
            addCriterion("command_size not like", value, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeIn(List<String> values) {
            addCriterion("command_size in", values, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeNotIn(List<String> values) {
            addCriterion("command_size not in", values, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeBetween(String value1, String value2) {
            addCriterion("command_size between", value1, value2, "commandSize");
            return (Criteria) this;
        }

        public Criteria andCommandSizeNotBetween(String value1, String value2) {
            addCriterion("command_size not between", value1, value2, "commandSize");
            return (Criteria) this;
        }

        public Criteria andUpPathIsNull() {
            addCriterion("up_path is null");
            return (Criteria) this;
        }

        public Criteria andUpPathIsNotNull() {
            addCriterion("up_path is not null");
            return (Criteria) this;
        }

        public Criteria andUpPathEqualTo(String value) {
            addCriterion("up_path =", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathNotEqualTo(String value) {
            addCriterion("up_path <>", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathGreaterThan(String value) {
            addCriterion("up_path >", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathGreaterThanOrEqualTo(String value) {
            addCriterion("up_path >=", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathLessThan(String value) {
            addCriterion("up_path <", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathLessThanOrEqualTo(String value) {
            addCriterion("up_path <=", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathLike(String value) {
            addCriterion("up_path like", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathNotLike(String value) {
            addCriterion("up_path not like", value, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathIn(List<String> values) {
            addCriterion("up_path in", values, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathNotIn(List<String> values) {
            addCriterion("up_path not in", values, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathBetween(String value1, String value2) {
            addCriterion("up_path between", value1, value2, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathNotBetween(String value1, String value2) {
            addCriterion("up_path not between", value1, value2, "upPath");
            return (Criteria) this;
        }

        public Criteria andUpPathNewIsNull() {
            addCriterion("up_path_new is null");
            return (Criteria) this;
        }

        public Criteria andUpPathNewIsNotNull() {
            addCriterion("up_path_new is not null");
            return (Criteria) this;
        }

        public Criteria andUpPathNewEqualTo(String value) {
            addCriterion("up_path_new =", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewNotEqualTo(String value) {
            addCriterion("up_path_new <>", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewGreaterThan(String value) {
            addCriterion("up_path_new >", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewGreaterThanOrEqualTo(String value) {
            addCriterion("up_path_new >=", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewLessThan(String value) {
            addCriterion("up_path_new <", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewLessThanOrEqualTo(String value) {
            addCriterion("up_path_new <=", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewLike(String value) {
            addCriterion("up_path_new like", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewNotLike(String value) {
            addCriterion("up_path_new not like", value, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewIn(List<String> values) {
            addCriterion("up_path_new in", values, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewNotIn(List<String> values) {
            addCriterion("up_path_new not in", values, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewBetween(String value1, String value2) {
            addCriterion("up_path_new between", value1, value2, "upPathNew");
            return (Criteria) this;
        }

        public Criteria andUpPathNewNotBetween(String value1, String value2) {
            addCriterion("up_path_new not between", value1, value2, "upPathNew");
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