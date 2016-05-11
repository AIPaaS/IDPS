package com.ai.paas.ipaas.image;

//接口定义

import java.io.InputStream;

public interface IImageClient {

	/**
	 * 上传图片
	 * 
	 * @param image图片字节数组
	 * @param name
	 *            文件名（备注）
	 * @return 图片id
	 */
	public String upLoadImage(byte[] image, String name);

	/**
	 * 
	 * @return 返回图片服务器外网地址
	 */
	public String getImgServerInterAddr();

	/**
	 * 
	 * @return 返回图片服务器内网地址
	 */
	public String getImgServerIntraAddr();

	/**
	 * 根据图片id 图片类型获取图片内网地址
	 * 
	 * @param imageId
	 *            图片id
	 * @param imageType
	 *            例如:.jpg .png
	 * @return
	 */
	public String getImageUrl(String imageId, String imageType);

	/**
	 * 获取图片上传地址---- 内网
	 * 
	 * @return
	 */
	public String getImageUploadUrl();

	/**
	 * 删除图片
	 * 
	 * @param imageId
	 * @return
	 */
	public boolean deleteImage(String imageId);

	/**
	 * 下载图片，不负责关闭输入流
	 * 
	 * @param imageId
	 *            图片id
	 * @param imageType
	 *            图片类型
	 * @param imageScale
	 *            图片尺寸 例如：800x800
	 * @return
	 */
	public InputStream getImageStream(String imageId, String imageType,
			String imageScale);

	/**
	 * 下载图片，不负责关闭输入流
	 * @param imageId 图片id
	 * @param imageType 图片类型
	 * @param imageScale 图片尺寸 例如：800x800
	 * @return
	 */
	public byte[] getImage(String imageId, String imageType, String imageScale);

}