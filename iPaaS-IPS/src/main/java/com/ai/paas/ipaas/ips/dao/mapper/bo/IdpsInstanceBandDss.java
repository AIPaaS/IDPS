package com.ai.paas.ipaas.ips.dao.mapper.bo;

public class IdpsInstanceBandDss {
    private Integer id;

    private String userId;

    private String serviceId;

    private String dssServiceId;

    private String dssPid;

    private String dssServicePwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getDssServiceId() {
        return dssServiceId;
    }

    public void setDssServiceId(String dssServiceId) {
        this.dssServiceId = dssServiceId == null ? null : dssServiceId.trim();
    }

    public String getDssPid() {
        return dssPid;
    }

    public void setDssPid(String dssPid) {
        this.dssPid = dssPid == null ? null : dssPid.trim();
    }

    public String getDssServicePwd() {
        return dssServicePwd;
    }

    public void setDssServicePwd(String dssServicePwd) {
        this.dssServicePwd = dssServicePwd == null ? null : dssServicePwd.trim();
    }
}