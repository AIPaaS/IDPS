package com.ai.paas.ipaas.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.ai.paas.ipaas.PaasContextHolder;
import com.ai.paas.ipaas.ips.IImage;
import com.ai.paas.ipaas.ips.impl.GMImage;

public class ImageUtil {
	private static final Logger log = Logger.getLogger(ImageUtil.class);

	private static IImage  imageSv;
	
	
	private static String config;
	private static String pid;
	private static String pwd;
	private static String srvId;
	
	public ImageUtil(String configStr ,String userPid,String servicePwd,String serviceId ){
		config = configStr;
		pid = userPid;
		pwd = servicePwd;
		srvId = serviceId;
	}
	private static IImage getIntance() {
		
		if (null != imageSv)
			return imageSv;
		else {
			imageSv =  new GMImage(config,pid,pwd,srvId) ;
			return imageSv;
		}
	}
	
	
	
	/**
	 * 1、本地原图是否存在
	 * 2、存在时，生成缩略图；不存在，从mongodb获得原图保存在本地，并生成缩略图
	 * @param uri  请求串
	 * @param imageName 缩放后图片的路径图片名称
	 * @param type  1为比例处理，2为大小处理，如（比例：1024x1024,大小：50%x50%）
	 * @param imageSize 缩放后的图片尺寸 如（比例：1024x1024,大小：50%x50%）
	 * @param imageType 图片类型
	 * @param isExtent  缩略图是否填充空白
	 * @throws Exception
	 */
	public static String getScaleImage(String uri,String imageName,String imageSize,String imageType,boolean isExtent) throws Exception{
		long begin = System.currentTimeMillis();
		//处理图片路径
		boolean localExist = getIntance().localImageExist(imageName,imageType);
		log.debug(uri+"--------localExist---------:"+localExist+" 耗时"+(System.currentTimeMillis()-begin));
		
		if(!localExist){
			begin = System.currentTimeMillis();
			getIntance().getRomteImage(imageName,imageType);
			log.debug(uri+"--------getRomteImage---------"+" 耗时"+(System.currentTimeMillis()-begin));

		}
		String path = null;
		if(imageSize == null){
			//源图
			path = getIntance().getSourceImagePath(imageName,imageType);
		}else{
			path = getIntance().scaleImage(uri,imageName, 1, imageSize, imageType,isExtent);
		}
		log.debug(uri+"--------getScaleImage---------:"+localExist+" 耗时"+(System.currentTimeMillis()-begin));
		return path;

	}
	
	/**是否使用GM模式
	 * @return
	 */
	public static boolean getGMMode(){
		return getIntance().getGMMode();
	}
	/**
	 * 异常时， 图片路径
	 * @return
	 */
	public static String getReservePath(){
		return getIntance().getReservePath();
	}
	
	public static boolean supportService(String imageType){
		return getIntance().supportService(imageType);
	}
	/**
	 * 保存文件到mongodb（建议图片存储不要使用该方法）
	 * 
	 * @param byteFile 图片文件或普通文件
	 * @param imageName 图片名称  建议使用商品标识
	 * @param imageType 图片格式 例如doc，zip
	 * @throws Exception
	 */
	public static String saveToRomte(File image,String imageName, String imageType) throws Exception{
		return getIntance().saveToRomte(image, imageName, imageType);
	}
	
	/**
	 * 清除相关产品的静态页缓存（varnish、redis）
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public static boolean removeCacheHtml(final String productId){
		try {
			return	getIntance().removeCacheHtml(productId);
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}
	
	/**
	 * 异步清除相关产品的静态页缓存（varnish、redis）   业务前店组调用
	 * 除非有异常抛出，否则会返回true
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public static boolean removeCacheHtmlDelay(final String productId){
//		try {
//			return	getDelayInstance().removeCacheHtml(productId);
//		} catch (Exception e) {
//			log.error("", e);
//			return false;
//		}
		return true;
	}

	/**
	 * 上传图片时，转换图片格式
	 * @param srcImage 不带路径
	 * @param descImage
	 * @throws Exception
	 */
	public static void convertType(String srcImage,String descImage) throws Exception{
		getIntance().convertType(srcImage, descImage);
	}
	
	/**
	 * 上传图片  本地存放路径
	 * @return
	 * @throws Exception
	 */
	public static String getUpPath(){
		return getIntance().getUpPath();
	}
	/**
	 * 上传图片  转换格式后的本地路径
	 * @return
	 * @throws Exception
	 */
	public static String getUpPathNew(){
		return getIntance().getUpPathNew();
	}
	/**
	 * 支持的图片格式类型
	 * @return
	 * @throws Exception
	 */
	public static String getSupportType(){
		return getIntance().getSupportType();
	}
	
	/**
	 * 保存文件到mongodb（建议图片存储不要使用该方法）
	 * 
	 * @param byteFile 图片文件或普通文件
	 * @param imageName 图片名称  建议使用商品标识
	 * @param imageType 图片格式 例如doc，zip
	 * @throws Exception
	 */
//	public static String saveToRomte(byte[] byteFile,String imageName, String imageType) throws Exception{
//		return MongoFileUtil.saveFile(byteFile, imageName, imageType);
//	}
	/**
	 * 删除mongoDB中的图片
	 * @param id 
	 */
//	public static void deleteImage(String id){
//		MongoFileUtil.deleteFile(id);
//	}
	/**
	 * 图片上传至图片服务器
	 * 支持上传的图片格式jpg,jpeg,png,bmp,gif,tiff,raw,pcx,tga,fpx  图片服务器会统一处理成jpg格式。
	 * @param image		byte[]图片字节数组
	 * @param name      图片名称 如：1.jpg  不要带路径
	 * @return
	 */
//	public static String upLoadImage(byte[] image,String name){
//		if(image == null)
//			return null;
//		if(image.length>5*1024*1024){
//			log.error("上传的图片大于5M!");
//			return null;
//		}
//		String id = null;
//		String upUrl = getUpImageUrl();
//		if(upUrl == null || upUrl.length() == 0){
//			log.error("请检查配置,上传图片的服务器地址。");
//			return null;
//		}
//		String result = HttpUtil.upImage(getUpImageUrl(), image,name);
//		Map<String ,String>  json  = GsonUtil.gsonToObject(result, Map.class);
//		if("success".equals(json.get("result"))){
//			id = json.get("id");
//		}
//		return id;
//	}
	
	/**获取图片服务器地址
	 * @return
	 */
//	public static String getImageServer(){
//		return getIntance().getImageServer();
//	}
//	
//	/**获取图片服务器地址--内网url
//	 * @return
//	 */
//	public static String getImageServerForIn(){
//		return getIntance().getImageUploadUrl();
//	}
	
	/**获取调用图片的url
	 * @param imageId 图片id 例如53b0dfaf300418069bc01193，53b0dfaf300418069bc01193_100x100
	 * @return
	 */
//	public static String getImageUrl(String imageId){
//		return getImageServer()+"/image/"+imageId+getSupportType();
//	}
//	
//	/**获取调用文本、附件的url
//	 * @param id  文本、附件的id 例如：53b6a32d09a9797981973489
//	 * @param type 文本、附件的浏览类型，页面直接显示还是下载：doc是下载，html直接显示，为空直接显示
//	 * @return
//	 */
//	public static String getStaticDocUrl(String id,String type){
//		return getImageServer()+"/static/"+((StringUtils.isEmpty(type)||"html".equals(type))?"html/":"attch/")+id;
//	}
//	
//	/**获取商品缓存页面的url
//	 * @param id  商品ID
//	 * @param type 文本、附件的浏览类型，页面直接显示还是下载：doc是下载，html直接显示，为空直接显示
//	 * @return
//	 */
//	public static String getRemoveStaticUrl(String id){
//		return getImageServer()+"/removestatic/cacheService/removeCacheHtml/"+id;
//	}
//	
//	/**获取上传图片的url
//	 * @return
//	 */
//	public static String getUpImageUrl(){
//		return getImageServerForIn()+"/upImage";
//	}
	
	public static void main(String[] args) {
		String fileStr = "/Users/liwenxian/Downloads/1103/test/dxf.jpg";
		File file  = new File(fileStr);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			byte[] bytes = new byte[input.available()];
			input.read(bytes);
//			System.out.println(upLoadImage(bytes, "dxf.jpg"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
		
	}
}
