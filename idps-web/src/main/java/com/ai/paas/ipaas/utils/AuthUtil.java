package com.ai.paas.ipaas.utils;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ai.paas.ipaas.image.ImageAuthDescriptor;
import com.ai.paas.ipaas.ips.AuthConstant;
import com.ai.paas.ipaas.util.StringUtil;

public class AuthUtil {
	private static final Logger log = LogManager.getLogger(AuthUtil.class);

	private AuthUtil() {
	}

	public static ImageAuthDescriptor getAuthInfo() {
		// 获取相应的认证信息，先从环境变量中取，然后从系统属性中取
		ImageAuthDescriptor auth = new ImageAuthDescriptor();
		//先取属性文件，属性文件默认是服务模式，没问题
		auth = getIDPSInfoFromProps(auth);
		auth.setCompMode(null != System.getenv(AuthConstant.IS_COMP_MODE)
				&& "true".equalsIgnoreCase(System
						.getenv(AuthConstant.IS_COMP_MODE)) ? true : false);
		auth = getAuthInfoFromEnv(auth);
		auth.setCompMode(null != System.getenv(AuthConstant.IS_COMP_MODE)
				&& "true".equalsIgnoreCase(System
						.getProperty(AuthConstant.IS_COMP_MODE)) ? true : false);
		auth = getAuthInfoFromSysProps(auth);
		
		return auth;
	}

	private static ImageAuthDescriptor getAuthInfoFromEnv(
			ImageAuthDescriptor auth) {
		if (!auth.isCompMode()) {
			if (!StringUtil.isBlank(System.getenv(AuthConstant.AUTH_URL)))
				auth.setAuthAdress(System.getenv(AuthConstant.AUTH_URL));
			if (!StringUtil.isBlank(System.getenv(AuthConstant.AUTH_USER_PID)))
				auth.setPid(System.getenv(AuthConstant.AUTH_USER_PID));
			if (!StringUtil.isBlank(System.getenv(AuthConstant.AUTH_SRV_ID)))
				auth.setServiceId(System.getenv(AuthConstant.AUTH_SRV_ID));
			if (!StringUtil.isBlank(System.getenv(AuthConstant.AUTH_SRV_PWD)))
				auth.setPassword(System.getenv(AuthConstant.AUTH_SRV_PWD));
		} else {
			if (!StringUtil.isBlank(System.getenv(AuthConstant.MONGO_INFO)))
				auth.setMongoInfo(System.getenv(AuthConstant.MONGO_INFO));
		}
		return auth;
	}

	private static ImageAuthDescriptor getAuthInfoFromSysProps(
			ImageAuthDescriptor auth) {
		if (!auth.isCompMode()) {
			if (!StringUtil.isBlank(System.getProperty(AuthConstant.AUTH_URL)))
				auth.setAuthAdress(System.getProperty(AuthConstant.AUTH_URL));
			if (!StringUtil.isBlank(System
					.getProperty(AuthConstant.AUTH_USER_PID)))
				auth.setPid(System.getProperty(AuthConstant.AUTH_USER_PID));
			if (!StringUtil.isBlank(System
					.getProperty(AuthConstant.AUTH_SRV_ID)))
				auth.setServiceId(System.getProperty(AuthConstant.AUTH_SRV_ID));
			if (!StringUtil.isBlank(System
					.getProperty(AuthConstant.AUTH_SRV_PWD)))
				auth.setPassword(System.getProperty(AuthConstant.AUTH_SRV_PWD));
		} else {
			auth.setMongoInfo(System.getProperty(AuthConstant.MONGO_INFO));
		}
		return auth;
	}

	private static ImageAuthDescriptor getIDPSInfoFromProps(
			ImageAuthDescriptor auth) {
		try {
			Properties props = new Properties();
			props.load(AuthUtil.class.getClassLoader().getResourceAsStream(
					"idps.properties"));
			auth.setCompMode(null != props
					.getProperty(AuthConstant.IS_COMP_MODE)
					&& "true".equalsIgnoreCase(props
							.getProperty(AuthConstant.IS_COMP_MODE)) ? true
					: false);
			auth.setMongoInfo(props.getProperty(AuthConstant.MONGO_INFO));
		} catch (Exception e) {
			log.info("", e);
		}
		return auth;
	}
}
