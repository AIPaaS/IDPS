package com.ai.paas.ipaas.utils;

import java.util.Properties;

import com.ai.paas.ipaas.ips.AuthConstant;
import com.ai.paas.ipaas.util.StringUtil;

public class AuthUtil {
	private AuthUtil() {
	}

	public static SubAuthDescriptor getAuthInfo() {
		// 获取相应的认证信息，先从环境变量中取，然后从系统属性中取
		SubAuthDescriptor auth = null;
		auth = getAuthInfoFromEnv();
		if (null != auth) {
			return auth;
		}
		if (null != getAuthInfoFromProps()) {
			auth = getAuthInfoFromProps();
			return auth;
		}
		if(null != getMongoInfoFromProps()) {
			auth = getMongoInfoFromProps();
		}
		
		return auth;
	}

	private static SubAuthDescriptor getAuthInfoFromEnv() {
		SubAuthDescriptor auth = null;
		if (!StringUtil.isBlank(System.getenv(AuthConstant.AUTH_URL))) {
			auth = new SubAuthDescriptor();
			auth.setAuthAdress(System.getenv(AuthConstant.AUTH_URL));
			auth.setPid(System.getenv(AuthConstant.AUTH_USER_PID));
			auth.setServiceId(System.getenv(AuthConstant.AUTH_SRV_ID));
			auth.setPassword(System.getenv(AuthConstant.AUTH_SRV_PWD));
		}
		return auth;
	}

	private static SubAuthDescriptor getAuthInfoFromProps() {
		SubAuthDescriptor auth = null;
		if (!StringUtil.isBlank(System.getProperty(AuthConstant.AUTH_URL))) {
			auth = new SubAuthDescriptor();
			auth.setAuthAdress(System.getProperty(AuthConstant.AUTH_URL));
			auth.setPid(System.getProperty(AuthConstant.AUTH_USER_PID));
			auth.setServiceId(System.getProperty(AuthConstant.AUTH_SRV_ID));
			auth.setPassword(System.getProperty(AuthConstant.AUTH_SRV_PWD));
			auth.setIsNeedAuth(System.getProperty(AuthConstant.IS_NEED_AUTH));
			auth.setMongoInfo(System.getProperty(AuthConstant.MONGO_INFO));
		}
		return auth;
	}
	
	private static SubAuthDescriptor getMongoInfoFromProps() {
		SubAuthDescriptor auth = null;
		try {
			Properties props = new Properties();
			props.load(AuthUtil.class.getClassLoader().getResourceAsStream("mongo.properties"));
			auth = new SubAuthDescriptor();
			auth.setIsNeedAuth(props.getProperty(AuthConstant.IS_NEED_AUTH));
			auth.setMongoInfo(props.getProperty(AuthConstant.MONGO_INFO));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return auth;
	}
}
