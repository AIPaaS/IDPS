package com.ai.paas.ipaas.utils;

import com.ai.paas.ipaas.uac.vo.AuthDescriptor;

public class SubAuthDescriptor extends AuthDescriptor {
	private static final long serialVersionUID = -431793174759343176L;

	private String isNeedAuth = null;
	private String mongoInfo = null;

	public SubAuthDescriptor() {
	}
	
	public SubAuthDescriptor(String isNeedAuth, String mongoInfo) {
		this.isNeedAuth = isNeedAuth;
		this.mongoInfo = mongoInfo;
	}
	
	public String getIsNeedAuth() {
		return isNeedAuth;
	}

	public void setIsNeedAuth(String isNeedAuth) {
		this.isNeedAuth = isNeedAuth;
	}

	public String getMongoInfo() {
		return mongoInfo;
	}

	public void setMongoInfo(String mongoInfo) {
		this.mongoInfo = mongoInfo;
	}
}
