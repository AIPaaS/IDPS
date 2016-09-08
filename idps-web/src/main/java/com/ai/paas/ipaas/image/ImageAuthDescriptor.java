package com.ai.paas.ipaas.image;

import com.ai.paas.ipaas.uac.vo.AuthDescriptor;

public class ImageAuthDescriptor extends AuthDescriptor {
	private static final long serialVersionUID = -431793174759343176L;

	/**
	 * 是否为组件模式
	 */
	private boolean isCompMode = false;
	private String mongoInfo = null;

	public ImageAuthDescriptor() {
	}

	public ImageAuthDescriptor(boolean isCompMode, String mongoInfo) {
		this.isCompMode = isCompMode;
		this.mongoInfo = mongoInfo;
	}

	public String getMongoInfo() {
		return mongoInfo;
	}

	public void setMongoInfo(String mongoInfo) {
		this.mongoInfo = mongoInfo;
	}

	public boolean isCompMode() {
		return isCompMode;
	}

	public void setCompMode(boolean isCompMode) {
		this.isCompMode = isCompMode;
	}

}
