
package com.ai.paas.ipaas.ips.impl;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.ai.paas.ipaas.config.ConfigurationCenter;
import com.ai.paas.ipaas.config.ConfigurationWatcher;
import com.ai.paas.ipaas.ips.IImage;
import com.ai.paas.ipaas.utils.JSONValidator;
import com.ai.paas.ipaas.utils.PaasException;
import com.google.gson.Gson;

public class GMImage implements ConfigurationWatcher,IImage {
	
	private static final Logger log = Logger.getLogger(GMImage.class);
	private String confPath = "";
	
	private static final String GM_PATH_KEY = "gmPath";
	private static final String MAX_ACTIVE_KEY = "maxActive";
	private static final String MAX_WAIT_KEY = "maxWait";
	private static final String MAX_IDLE_KEY = "maxIdle";
	private static final String TEST_ON_BORROW_KEY = "testOnBorrow";
	private static final String TEST_ON_RETURN_KEY = "testOnReturn";
	private static final String IMAGE_ROOT = "imageRoot";
	private static final String IMAGE_ROOT_NEW = "imageRootNew";
	private static final String IMAGE_NAME_SPLIT = "imageNameSplit"; 
	private static final String IMAGE_TYPE = "imageType"; 
	private static final String GM_MODE_KEY = "gmMode";
	private static final String RESERVE_IMAGE_KEY = "reserveImage";
	private static final String EXTENT_KEY = "extent"; 
	private static final String QUALITY_KEY = "quality"; 
	
	private static final String REDISKEYPREFIX_KEY = "redisKeyPrefix"; 
	private static final String VARNISHSERVER_KEY = "varnishServer"; 
	private static final String COMMANDSIZE_KEY = "commandSize"; 
	private static final String PRIMARYPARAM_KEY = "primaryParam"; 
	private static final String UPPATH_KEY = "upPath"; 
	private static final String UPPATHNEW_KEY = "upPathNew"; 
	
//	private static final String SERVERURL_KEY = "serverUrl"; 
//	private static final String UPLOADURL_KEY = "uploadUrl"; 
	
	private String  gmPath = null;
	
	//本地保存图片的路径 源图
	private String imageRoot = null;
	//图片名分隔符 _
	private String imageNameSplit = null;
	//图片格式 .jpg等
	private String imageType = null;
	//本地保存图片的路径 缩略图
	private String imageRootNew = null;	
	//是否补白尺寸图
	private boolean extent = true;
	//图片质量
	private int quality = 90; 
		
	private int maxActive;
	private long maxWait;
	private int maxIdle;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	
	private String redisKeyPrefix;
	private String varnishServer;
	//与varnish一次连接，清除缓存url的个数
	private int commandSize;
	private String primaryParam;
	//是否开启graphicsmagick模式
	private boolean gmMode = true;
	//异常时，返回异常图片的路径
	private String reserveImage = null;
	private List<String> types = null;
	//上传图片  本地存放路径
	private String upPath;
	//上传图片  转换格式后的本地路径
	private String upPathNew;
	//图片服务器地址
//	private String serverUrl;
//	//图片上传，使用内网地址
//	private String uploadUrl;
	
	private GMClient gMClient;
	private ConfigurationCenter confCenter = null;
	private String userPid;
	private String servicePwd;
	private String serviceId;

	public void init(String config,String userPid,String servicePwd,String serviceId) {
		this.userPid= userPid;
		this.servicePwd = servicePwd;
		this.serviceId = serviceId;
		process(config);
	}

	public GMImage(String config,String userPid,String servicePwd,String serviceId){
		init(config,userPid,servicePwd,serviceId);
	}
	
	@Override
	public boolean localImageExist(String localPath,String imageType) throws Exception {
		return gMClient.localImageExist(localPath,imageType);
	}

	@Override
	public void getRomteImage(String imageId,String imageType) throws Exception {
		gMClient.getRomteImage(imageId, imageType);
	}

	@Override
	public String scaleImage(String uri, String imageName, int type,
			String imageSize, String imageType,boolean isExtent) throws Exception {
		return gMClient.scaleImage(uri, imageName, type, imageSize, imageType,isExtent);
	}

	@Override
	public void addImgText(String srcPath, String newPath) throws Exception {
		gMClient.addImgText(srcPath, newPath);
	}

	@Override
	public void saveCacheImage(String imageName, String imageSize,
			String newPath,String imageType) throws Exception {
//		gMClient.saveCacheImage(imageName, imageSize, newPath, imageType);
	}

	@Override
	public void removeLocalCutedImage(String path) throws Exception {
		gMClient.removeLocalCutedImage(path);
	}

	@Override
	public String saveToRomte(File image,String imageName, String imageType) throws Exception {
		return "";
	}

	@Override
	public void process(String conf) {
		if (log.isInfoEnabled()) {
			log.info("new log configuration is received: " + conf);
		}
		gMClient = new GMClient(conf,userPid,servicePwd,serviceId);
		
		Map<String,String> jsonObj = new HashMap<String,String>();
		Gson gson = new Gson();
		jsonObj = gson.fromJson(conf, jsonObj.getClass());
		gmPath = jsonObj.get(GM_PATH_KEY).toString();
		maxActive =Integer.parseInt( jsonObj.get(MAX_ACTIVE_KEY));
		maxWait =Long.parseLong(jsonObj.get(MAX_WAIT_KEY)) ;
		maxIdle = Integer.parseInt( jsonObj.get(MAX_IDLE_KEY));
		testOnBorrow =Boolean.valueOf(jsonObj.get(TEST_ON_BORROW_KEY)) ;
		testOnReturn = Boolean.valueOf(jsonObj.get(TEST_ON_RETURN_KEY));
		imageRoot = jsonObj.get(IMAGE_ROOT);
		imageRootNew = jsonObj.get(IMAGE_ROOT_NEW);
		imageNameSplit = jsonObj.get(IMAGE_NAME_SPLIT);
		if(imageType==null||"".equals(imageType)){
			types = Arrays.asList(new String[]{".jpg"});
		}else{
			types = Arrays.asList(imageType.split(","));
		}
		extent =Boolean.valueOf(jsonObj.get(EXTENT_KEY));
		quality =Integer.parseInt( jsonObj.get(QUALITY_KEY));
		gmMode = Boolean.valueOf(jsonObj.get(GM_MODE_KEY));
		reserveImage = jsonObj.get(RESERVE_IMAGE_KEY);
		redisKeyPrefix = jsonObj.get(REDISKEYPREFIX_KEY);
		upPath = jsonObj.get(UPPATH_KEY);
		upPathNew = jsonObj.get(UPPATHNEW_KEY);
	
		
		
		
		if (log.isInfoEnabled()) {        
			log.info("gm server address is changed to " + conf);
		}
	}
	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public ConfigurationCenter getConfCenter() {
		return confCenter;
	}

	public void setConfCenter(ConfigurationCenter confCenter) {
		this.confCenter = confCenter;
	}

	@Override
	public String getSourceImagePath(String imageName,String imageType) throws Exception {
		return gMClient.getSourceImagePath(imageName, imageType);
	}

	@Override
	public boolean getGMMode() {
		return gmMode;
	}

	@Override
	public String getReservePath() {
		return reserveImage;
	}

	@Override 
	public boolean supportService(String imageTypeReq) {
		return types.contains(imageTypeReq);
	}

	@Override
	public boolean removeCacheHtml(String productId) throws Exception {
		return true;
	}

	@Override
	public void convertType(String srcImage, String descImage) throws Exception {
		String src = upPath.endsWith(File.separator)?(upPath+srcImage):(upPath+File.separator+srcImage);
		String desc = upPathNew.endsWith(File.separator)?(upPathNew+descImage):(upPathNew+File.separator+descImage);
		gMClient.convertType(src,desc);
	}

	@Override
	public String getUpPath(){
		return upPath;
	}

	@Override
	public String getUpPathNew(){
		return upPathNew;
	}

	@Override
	public String getSupportType(){
		if(types!=null&&types.size()>0)
			return types.get(0);
		else
			return imageType;
	}

//	@Override
//	public String getImageServer() {
//		return serverUrl;
//	}
//
//	@Override
//	public String getImageUploadUrl() {
//		return uploadUrl;
//	}
	

	
}
