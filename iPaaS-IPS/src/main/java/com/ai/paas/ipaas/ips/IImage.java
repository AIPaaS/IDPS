package com.ai.paas.ipaas.ips;

import java.io.File;

/**
 * 处理图片(分布式存储图片＋动态缩略图)
 * @author liwx3
 *
 */
public interface IImage {
	/**
	 * 判断本地图片是否存在
	 * @param localPath
	 * @throws Exception
	 * @return
	 */
	boolean localImageExist(String localPath,String imageType) throws Exception;
	
	/**
	 * 获得图片存储系统中的图片，并保存在本地
	 * @param imageId
	 * @param localPath
	 * @throws Exception
	 */
	void getRomteImage(String imageId,String imageType) throws Exception;
	
	/**
	 * 裁剪图片
	 * @param uri  请求串
	 * @param imageName 缩放后图片的路径图片名称
	 * @param type  1为比例处理，2为大小处理，如（比例：1024x1024,大小：50%x50%）
	 * @param imageSize 缩放后的图片尺寸 如（比例：1024x1024,大小：50%x50%）
	 * @param imageType 图片类型
	 * @param isExtent  缩略图是否填充空白
	 * @throws Exception
	 */
	String scaleImage(String uri,String imageName, int type, String imageSize,String imageType,boolean isExtent) throws Exception;
	
	
	/**
	 * 获得本地源图完整路径
	 * @param path
	 * @throws Exception
	 */
	String getSourceImagePath(String imageName,String imageType) throws Exception;
	
	/**是否使用GM模式
	 * @return
	 */
	boolean getGMMode();
	/**
	 * 异常时， 图片路径
	 * @return
	 */
	String getReservePath();
	/**图片格式是否支持
	 * @return
	 */
	boolean supportService(String imageType);
	
	/**
	 * 保存图片到mongodb
	 * @param image 图片文件
	 * @param imageName 图片名称  建议使用商品标识
	 * @param imageType 图片格式 例如jpg,png
	 * @throws Exception
	 */
	String saveToRomte(File image,String imageName, String imageType) throws Exception;
	
	
	/**
	 * 清除相关产品的静态页缓存（varnish、redis）
	 * @param productId
	 * @param params
	 * @return
	 * @throws Exception
	 */
	boolean removeCacheHtml(String productId) throws Exception;
	/**
	 * 上传图片时，转换图片格式
	 * @param srcImage
	 * @param descImage
	 * @throws Exception
	 */
	void convertType(String srcImage,String descImage) throws Exception;
	
	/**
	 * 上传图片  本地存放路径
	 * @return
	 * @throws Exception
	 */
	String getUpPath();
	/**
	 * 上传图片  转换格式后的本地路径
	 * @return
	 * @throws Exception
	 */
	String getUpPathNew();
	/**
	 * 支持的图片格式类型
	 * @return
	 * @throws Exception
	 */
	String getSupportType();
	
	/**
	 * 给图片加水印
	 * @param srcPath 源图片路径
	 * @param newPath 加水印后图片的路径
	 * @throws Exception
	 */
	void addImgText(String srcPath,String newPath) throws Exception;
	
	
	/**
	 * 将缩略图放入缓存
	 * @param newPath
	 * @throws Exception
	 */
	void saveCacheImage(String imageName,String imageSize,String newPath,String imageType) throws Exception;
	
	/**
	 * 删除本地生成的缩略图
	 * @param path
	 */
	void removeLocalCutedImage(String path) throws Exception;
	
	/**获取图片服务器地址
	 * @return
	 */
	String getImageServer();
	/**获取图片服务器地址  上传时，使用内网URL
	 * @return
	 */
	String getImageUploadUrl();
}
