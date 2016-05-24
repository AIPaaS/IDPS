package com.ai.paas.ipaas.ips.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ccs.constants.ConfigException;
import com.ai.paas.ipaas.ccs.inner.CCSComponentFactory;
import com.ai.paas.ipaas.ccs.inner.ICCSComponent;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher.Event.KeeperState;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcherEvent;
import com.ai.paas.ipaas.ips.service.IImageService;
import com.ai.paas.ipaas.uac.service.UserClientFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.uac.vo.AuthResult;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.ResourceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GMImageServiceImpl implements IImageService {

	private static transient final Logger log = LogManager
			.getLogger(GMImageServiceImpl.class);
	private String confPath = "";

	private static final String GM_MODE_KEY = "gmMode";
	private static final String RESERVE_IMAGE_KEY = "reserveImage";

	// 图片格式 .jpg等
	private String imageType = null;

	// 是否开启graphicsmagick模式
	private boolean gmMode = true;
	// 异常时，返回异常图片的路径
	private String reserveImage = null;
	private List<String> types = null;
	// 上传图片 本地存放路径
	private String uploadPath;
	// 上传图片 转换格式后的本地路径
	private String destPath;

	private GMClient gmClient;
	private AuthDescriptor ad = null;
	private ICCSComponent ccsClient = null;

	private ConfigWatcher configWatcher = new ConfigWatcher() {
		public void processEvent(ConfigWatcherEvent event) {
			if (event == null) {
				// 不做什么
				return;
			}
			// 连接状态
			ConfigWatcher.Event.KeeperState keeperState = event.getState();
			// 事件类型
			ConfigWatcher.Event.EventType eventType = event.getType();
			if (ConfigWatcher.Event.EventType.NodeDataChanged == eventType) {
				// 监控到内容变化，实现算法
				if (log.isInfoEnabled()) {
					log.info("Get ZK node children num chanage event!");
				}
				processConfig();
			}
			// 可能存在接收到其他事件，比如断开了
			if (KeeperState.Disconnected == keeperState
					|| ConfigWatcher.Event.KeeperState.Expired == keeperState) {
				// 断开了，由于是临时节点，因此不管了。其他应该可以增加
			}
		}

	};

	public void init(AuthDescriptor ad) {
		this.ad = ad;
		AuthResult authResult = UserClientFactory.getUserClient().auth(ad);
		Assert.notNull(authResult, ResourceUtil
				.getMessage("com.ai.paas.ipaas.common.auth_result_null"));

		// 开始初始化
		Assert.notNull(authResult.getConfigAddr(), ResourceUtil
				.getMessage("com.ai.paas.ipaas.common.zk_addr_null"));
		Assert.notNull(authResult.getConfigUser(), ResourceUtil
				.getMessage("com.ai.paas.ipaas.common.zk_user_null"));
		Assert.notNull(authResult.getConfigPasswd(), ResourceUtil
				.getMessage("com.ai.paas.ipaas.common.zk_passwd_null"));

		// 获取内部zk地址后取得配置信息，返回json

		try {
			ccsClient = CCSComponentFactory.getConfigClient(
					authResult.getConfigAddr(), authResult.getConfigUser(),
					authResult.getConfigPasswd());
		} catch (ConfigException e) {
			throw new PaasRuntimeException(" get GM config info error!", e);
		}
		processConfig();
	}

	public GMImageServiceImpl(AuthDescriptor ad) {
		init(ad);
	}

	@Override
	public boolean isLocalImageExist(String localPath, String imageType)
			throws Exception {
		return gmClient.isLocalImageExist(localPath, imageType);
	}

	@Override
	public void getRomteImage(String imageId, String imageType)
			throws Exception {
		gmClient.getRomteImage(imageId, imageType);
	}

	@Override
	public String scaleImage(String uri, String imageName, int type,
			String imageSize, String imageType, boolean isExtent)
			throws Exception {
		return gmClient.scaleImage(uri, imageName, type, imageSize, imageType,
				isExtent);
	}

	@Override
	public void addImgText(String srcPath, String newPath) throws Exception {
		gmClient.addImgText(srcPath, newPath);
	}

	@Override
	public void removeLocalSizedImage(String path) throws Exception {
		gmClient.removeLocalSizedImage(path);
	}

	private void processConfig() {
		// GM的配置信息路径
		String config = null;

		try {
			//TODO add gm ccs path
			config = ccsClient.get("", configWatcher);
		} catch (ConfigException e) {
			log.error("", e);
		}
		if (log.isInfoEnabled()) {
			log.info("new log configuration is received: " + config);
		}
		gmClient = new GMClient(config, ad);
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(config, JsonObject.class);
		if (imageType == null || "".equals(imageType)) {
			types = Arrays.asList(new String[] { ".jpg" });
		} else {
			types = Arrays.asList(imageType.split(","));
		}
		gmMode = json.get(GM_MODE_KEY).getAsBoolean();
		reserveImage = json.get(RESERVE_IMAGE_KEY).getAsString();

		if (log.isInfoEnabled()) {
			log.info("gm config info is changed to " + config);
		}
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	@Override
	public String getSourceImagePath(String imageName, String imageType)
			throws Exception {
		return gmClient.getSourceImagePath(imageName, imageType);
	}

	@Override
	public boolean isGMMode() {
		return gmMode;
	}

	@Override
	public String getReservePath() {
		return reserveImage;
	}

	@Override
	public boolean isSupported(String imageType) {
		return types.contains(imageType);
	}

	@Override
	public void convertType(String srcImage, String destImage) throws Exception {
		String src = uploadPath.endsWith(File.separator) ? (uploadPath + srcImage)
				: (uploadPath + File.separator + srcImage);
		String dest = destPath.endsWith(File.separator) ? (destPath + destImage)
				: (destPath + File.separator + destImage);
		gmClient.convertType(src, dest);
	}

	@Override
	public String getUplodPath() {
		return uploadPath;
	}

	@Override
	public String getDestPath() {
		return destPath;
	}

	@Override
	public String getSupportType() {
		if (types != null && types.size() > 0)
			return types.get(0);
		else
			return imageType;
	}
}
