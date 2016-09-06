package com.ai.paas.ipaas.image;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.inner.CCSComponentFactory;
import com.ai.paas.ipaas.ccs.inner.ICCSComponent;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher.Event.EventType;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcherEvent;
import com.ai.paas.ipaas.image.impl.ImageClientImpl;
import com.ai.paas.ipaas.uac.service.UserClientFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.uac.vo.AuthResult;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.utils.IdpsContant;
import com.google.gson.Gson;

public class ImageFactory {
	private static transient final Logger log = LoggerFactory.getLogger(ImageFactory.class);
	private static Map<String, IImageClient> imageClients = new ConcurrentHashMap<String, IImageClient>();
	private final static String SEARCH_CONFIG_PATH = "/IDPS/";
	public static final String IMAGE_URL_PATH = "IMAGEURL";
	public static final String IMAGE_URL_OUT_PATH = "/IMAGEURL_OUT";

	private ImageFactory() {
	}

	@SuppressWarnings("unchecked")
	public static IImageClient getClient(AuthDescriptor ad) throws Exception {
		IImageClient iImageClient = null;
		Gson gson = new Gson();
		log.info("Check Formal Parameter AuthDescriptor ...");
		Assert.notNull(ad, "AuthDescriptor is blank");
		Assert.notNull(ad.getServiceId(), "service_id is blank");
		String srvId = ad.getServiceId();
		String userPid = ad.getPid();
		if (imageClients.containsKey(userPid + "_" + srvId)) {
			iImageClient = imageClients.get(userPid + "_" + srvId);
			return iImageClient;
		}
		AuthResult authResult = UserClientFactory.getUserClient().auth(ad);
		Assert.notNull(authResult.getUserId(), "UserId is blank");
		// // 开始初始化
		Assert.notNull(authResult.getConfigAddr(), "ConfigAddr is blank");
		Assert.notNull(authResult.getConfigUser(), "ConfigUser is blank");
		Assert.notNull(authResult.getConfigPasswd(), "ConfigPasswd is blank");
		
		// // 获取内部zk地址后取得该用户的cache配置信息，返回JSON String
		// // 获取该用户申请的cache服务配置信息
		log.info("Get confBase&conf ...");
		String userId = authResult.getUserId();
		ICCSComponent client = CCSComponentFactory.getConfigClient(authResult.getConfigAddr(),
				authResult.getConfigUser(), authResult.getConfigPasswd());

		String imageConfig = CCSComponentFactory
				.getConfigClient(authResult.getConfigAddr(), authResult.getConfigUser(), authResult.getConfigPasswd())
				.get(SEARCH_CONFIG_PATH + srvId);
		Map<String, String> configM = new HashMap<String, String>();
		configM = gson.fromJson(imageConfig, configM.getClass());
		String imageUrl = configM.get(IMAGE_URL_PATH);
		// String imageUrl = "http://10.1.235.199:8086/iPaas-IDPS";
		IDPSWatch watch = new IDPSWatch(client, userPid, userId, srvId, ad.getPassword(), imageUrl);
		String imageUrlOutM = CCSComponentFactory
				.getConfigClient(authResult.getConfigAddr(), authResult.getConfigUser(), authResult.getConfigPasswd())
				.get(SEARCH_CONFIG_PATH + srvId + IMAGE_URL_OUT_PATH, watch);

		Map<String, String> configMO = new HashMap<String, String>();
		configMO = gson.fromJson(imageUrlOutM, Map.class);

		String imageUrlOut = configMO.get(IMAGE_URL_PATH);
		iImageClient = new ImageClientImpl(IdpsContant.NEED_AUTH, userPid, srvId, ad.getPassword(), "", imageUrl, imageUrlOut);
		imageClients.put(userPid + "_" + srvId, iImageClient);
		return iImageClient;
	}

	/**
	 * 
	 * 监听外网地址变化，随时更新客户端
	 *
	 */
	private static class IDPSWatch extends ConfigWatcher {
		private ICCSComponent client;
		private String userPid;
		@SuppressWarnings("unused")
		private String userId;
		private String serviceId;
		private String servicePwd;
		private String imageUrl;

		public IDPSWatch(ICCSComponent client, String userPid, String userId, String serviceId, String srvPwd,
				String imageUrl) {
			this.client = client;
			this.serviceId = serviceId;
			this.userPid = userPid;
			this.userId = userId;
			this.servicePwd = srvPwd;
			this.imageUrl = imageUrl;
		}

		@Override
		public void processEvent(ConfigWatcherEvent event) {
			if (event == null)
				return;
			// 事件类型
			EventType eventType = event.getType();
			if (EventType.NodeDataChanged == eventType) {
				log.info("------monitor--投票结果------NodeDataChanged--------");
				try {
					// 循环watch

					IImageClient iImageClient = null;

					String imageUrlOut = client.get(SEARCH_CONFIG_PATH + serviceId + IMAGE_URL_OUT_PATH, this);
					iImageClient = new ImageClientImpl(IdpsContant.NEED_AUTH, userPid, serviceId, servicePwd, "", imageUrl, imageUrlOut);
					imageClients.put(userPid + "_" + serviceId, iImageClient);
				} catch (PaasException e) {
					log.error("投票结果变化时，读取出错：" + e.getMessage(), e);
				}
			} else {
				log.info("---eventType---FinalWatch--" + eventType);
			}
		}
	}

}
