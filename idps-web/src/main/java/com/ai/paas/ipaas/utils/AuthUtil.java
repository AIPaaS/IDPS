package com.ai.paas.ipaas.utils;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.base.DSSBaseFactory;
import com.ai.paas.ipaas.dss.base.interfaces.IDSSClient;
import com.ai.paas.ipaas.ips.AuthConstant;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.util.StringUtil;

public class AuthUtil {
	private AuthUtil() {
	}

	public static AuthDescriptor getAuthInfo() {
		// 获取相应的认证信息，先从环境变量中取，然后从系统属性中取
		AuthDescriptor auth = null;
		auth = getAuthInfoFromEnv();
		if (null != getAuthInfoFromProps()) {
			auth = getAuthInfoFromProps();
		}
		
		return auth;
	}

	/** 根据认证信息，获取DSS的客户端。**/
	public static IDSSClient getDssClient(AuthDescriptor ad) throws Exception{
		return DSSFactory.getClient(ad);
	}
	
	/** 根据mongoInfo信息，获取MongoDB客户端。**/
	public static IDSSClient getDssBaseClient(String mongoInfo) throws Exception{
		return DSSBaseFactory.getClient(mongoInfo);
	}

	private static AuthDescriptor getAuthInfoFromEnv() {
		AuthDescriptor auth = null;
		if (!StringUtil.isBlank(System.getenv(AuthConstant.AUTH_URL))) {
			auth = new AuthDescriptor();
			auth.setAuthAdress(System.getenv(AuthConstant.AUTH_URL));
			auth.setPid(System.getenv(AuthConstant.AUTH_USER_PID));
			auth.setServiceId(System.getenv(AuthConstant.AUTH_SRV_ID));
			auth.setPassword(System.getenv(AuthConstant.AUTH_SRV_PWD));
		}
		return auth;
	}

	private static AuthDescriptor getAuthInfoFromProps() {
		AuthDescriptor auth = null;
		if (!StringUtil.isBlank(System.getProperty(AuthConstant.AUTH_URL))) {
			auth = new AuthDescriptor();
			auth.setAuthAdress(System.getProperty(AuthConstant.AUTH_URL));
			auth.setPid(System.getProperty(AuthConstant.AUTH_USER_PID));
			auth.setServiceId(System.getProperty(AuthConstant.AUTH_SRV_ID));
			auth.setPassword(System.getProperty(AuthConstant.AUTH_SRV_PWD));
		}
		return auth;
	}
}
