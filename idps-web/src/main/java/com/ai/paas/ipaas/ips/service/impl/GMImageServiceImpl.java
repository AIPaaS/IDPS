package com.ai.paas.ipaas.ips.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ai.paas.ipaas.ips.service.IImageService;
import com.ai.paas.ipaas.uac.service.UserClientFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.uac.vo.AuthResult;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.ResourceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GMImageServiceImpl implements IImageService {
	private static transient final Logger log = LogManager.getLogger(GMImageServiceImpl.class);
	
	private String confFile = "/idps.properties";
	private String confPath = "/com/ai/paas/ipaas/idps/conf";

	private static final String GM_MODE_KEY = "gmMode";
	private static final String RESERVE_IMAGE_KEY = "reserveImage";
	private static final String UPLOAD_PATH_KEY = "srcPath";
	private static final String DEST_PATH_KEY = "targetPath";
	private static final String IMAGE_TYPE_KEY = "imageType";

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

	public GMImageServiceImpl(AuthDescriptor ad) {
		init(ad);
	}
	
	public GMImageServiceImpl(String mongoInfo) {
		init(mongoInfo);
	}
	
	public void init(AuthDescriptor ad) {
		AuthResult authResult = UserClientFactory.getUserClient().auth(ad);
		Assert.notNull(authResult, ResourceUtil.getMessage("com.ai.paas.ipaas.common.auth_result_null"));
		Assert.notNull(authResult.getConfigAddr(), ResourceUtil.getMessage("com.ai.paas.ipaas.common.zk_addr_null"));
		Assert.notNull(authResult.getConfigUser(), ResourceUtil.getMessage("com.ai.paas.ipaas.common.zk_user_null"));
		Assert.notNull(authResult.getConfigPasswd(), ResourceUtil.getMessage("com.ai.paas.ipaas.common.zk_passwd_null"));

		String config = null;
		try {
			/** 加载并解析属性文件 **/
			Properties props = new Properties();
			props.load(this.getClass().getResourceAsStream(confFile));
			config = props.getProperty(confPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		gmClient = new GMClient(config, ad);

		processConfig(config);
	}

	public void init(String mongoInfo) {
		String config = null;
		try {
			/** 加载并解析属性文件 **/
			Properties props = new Properties();
			props.load(this.getClass().getResourceAsStream(confFile));
			config = props.getProperty(confPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		gmClient = new GMClient(config, mongoInfo);

		processConfig(config);
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

	private void processConfig(String config) {
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(config, JsonObject.class);
		
		imageType = json.get(IMAGE_TYPE_KEY).getAsString();
		if (imageType == null || "".equals(imageType)) {
			types = Arrays.asList(new String[] { ".jpg" });
		} else {
			types = Arrays.asList(imageType.split(","));
		}
		gmMode = json.get(GM_MODE_KEY).getAsBoolean();
		reserveImage = json.get(RESERVE_IMAGE_KEY).getAsString();
		uploadPath = json.get(UPLOAD_PATH_KEY).getAsString();
		destPath = json.get(DEST_PATH_KEY).getAsString();
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
	public String getSupportType(String ext) {
		log.debug("ext:" + ext + ", types:" + types);
		if (types != null && types.size() > 0) {
			if (types.contains(ext)) {
				return ext;
			} else {
				return types.get(0);
			}
		} else
			return imageType;
	}

	@Override
	public boolean judgeSize(String srcImage, int minWidth, int minHeight) {
		String src = uploadPath.endsWith(File.separator) ? (uploadPath + srcImage)
				: (uploadPath + File.separator + srcImage);
		return gmClient.judgeSize(src, minWidth, minHeight);
	}
}
