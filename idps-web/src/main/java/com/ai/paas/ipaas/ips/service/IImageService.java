package com.ai.paas.ipaas.ips.service;

/**
 * 处理图片(分布式存储图片＋动态缩略图)
 * 
 * @author douxf
 *
 */
public interface IImageService {
    /**
     * 判断本地图片是否存在
     * 
     * @param localPath
     * @throws Exception
     * @return
     */
    public boolean isLocalImageExist(String localPath, String imageType) throws Exception;

    /**
     * 获得图片存储系统中的图片，并保存在本地
     * 
     * @param imageId
     * @param localPath
     * @throws Exception
     */
    public void getRomteImage(String imageId, String imageType) throws Exception;

    /**
     * 裁剪图片
     * 
     * @param uri       请求串
     * @param imageName 缩放后图片的路径图片名称
     * @param type      1为按比例处理，2为按大小处理，如（比例：50%x50%,大小：1024x1024）
     * @param imageSize 缩放后的图片尺寸 如（比例：50%x50%,大小：1024x1024）
     * @param imageType 图片类型
     * @param isExtent  缩略图是否填充空白
     * @throws Exception
     */
    public String scaleImage(String uri, String imageName, int type, String imageSize, String imageType,
            boolean isExtent) throws Exception;

    /**
     * 获得本地源图完整路径
     * 
     * @param path
     * @throws Exception
     */
    public String getSourceImagePath(String imageName, String imageType) throws Exception;

    /**
     * 是否使用GM模式
     * 
     * @return
     */
    public boolean isGMMode();

    /**
     * 异常时， 图片路径
     * 
     * @return
     */
    public String getReservePath();

    /**
     * 图片格式是否支持
     * 
     * @return
     */
    public boolean isSupported(String imageType);

    /**
     * 上传图片时，转换图片格式
     * 
     * @param srcImage
     * @param desImage
     * @throws Exception
     */
    public void convertType(String srcImage, String desImage) throws Exception;

    /**
     * 上传图片 本地存放路径
     * 
     * @return
     * @throws Exception
     */
    public String getUplodPath();

    /**
     * 上传图片 转换格式后的本地路径
     * 
     * @return
     * @throws Exception
     */
    public String getDestPath();

    /**
     * 支持的图片格式类型
     * 
     * @return
     * @throws Exception
     */
    public String getSupportType(String ext);

    /**
     * 给图片加水印
     * 
     * @param srcPath 源图片路径
     * @param newPath 加水印后图片的路径
     * @throws Exception
     */
    public void addImgText(String srcPath, String newPath) throws Exception;

    /**
     * 删除本地生成的缩略图
     * 
     * @param path
     */
    public void removeLocalSizedImage(String path) throws Exception;

    /**
     * 判断原图是否符合尺寸要求
     * 
     * @param srcImage
     * @param minWidth
     * @param minHeight
     * @return
     */
    public boolean judgeSize(String srcImage, int minWidth, int minHeight);

}
