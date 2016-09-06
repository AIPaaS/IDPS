package com.ai.paas.ipaas.utils;

import com.ai.paas.ipaas.ips.AuthConstant;
import com.ai.paas.ipaas.util.StringUtil;

public class AuthUtil {
	private AuthUtil() {
	}

	public static SubAuthDescriptor getAuthInfo() {
		// 获取相应的认证信息，先从环境变量中取，然后从系统属性中取
		SubAuthDescriptor auth = null;
		auth = getAuthInfoFromEnv();
		if (null != getAuthInfoFromProps()) {
			auth = getAuthInfoFromProps();
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
			auth.setIsNeedAuth(System.getenv(AuthConstant.IS_NEED_AUTH));
			auth.setMongoInfo(System.getenv(AuthConstant.MONGO_INFO));
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
			auth.setIsNeedAuth(System.getenv(AuthConstant.IS_NEED_AUTH));
			auth.setMongoInfo(System.getenv(AuthConstant.MONGO_INFO));
		}
		return auth;
	}
}
