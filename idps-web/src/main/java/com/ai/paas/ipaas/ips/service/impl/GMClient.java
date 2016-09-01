package com.ai.paas.ipaas.ips.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.gm4java.engine.GMConnection;
import org.gm4java.engine.GMService;
import org.gm4java.engine.GMServiceException;
import org.gm4java.engine.support.GMConnectionPoolConfig;
import org.gm4java.engine.support.PooledGMService;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.base.DSSBaseFactory;
import com.ai.paas.ipaas.dss.base.interfaces.IDSSClient;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GMClient {
	private static transient final Logger log = Logger.getLogger(GMClient.class);

	private static final String GM_PATH_KEY = "gmPath";
	private static final String MAX_ACTIVE_KEY = "maxActive";
	private static final String MAX_WAIT_KEY = "maxWait";
	private static final String MAX_IDLE_KEY = "maxIdle";
	private static final String TEST_ON_BORROW_KEY = "testOnBorrow";
	private static final String TEST_ON_RETURN_KEY = "testOnReturn";
	private static final String IMAGE_SRC_ROOT = "srcPath";
	private static final String IMAGE_TARGET_ROOT = "targetPath";
	private static final String IMAGE_NAME_SPLIT = "imageNameSplit";
	private static final String EXTENT_KEY = "extent";
	private static final String QUALITY_KEY = "quality";

	private GMService gmService;
	
	// 本地保存图片的路径 源图
	private String imageSrcRoot = null;
	// 图片名分隔符 _
	private String imageNameSplit = null;
	// 本地保存图片的路径 缩略图
	private String imageTargetRoot = null;
	// 是否补白尺寸图
	private boolean extent = true;
	// 图片质量
	private int quality = 90;

	private Gson gson = new Gson();
	private IDSSClient dc = null;

	/**
	 * 使用服务认证模式，初始化GMClient，获取IDSSClient。
	 * @param parameter
	 * @param ad
	 */
	public GMClient(String parameter, AuthDescriptor ad) {
		try {
			dc = DSSFactory.getClient(ad);
			JsonObject paras = gson.fromJson(parameter, JsonObject.class);

			if (paras != null) {
				GMConnectionPoolConfig config = new GMConnectionPoolConfig();
				config.setMaxActive(paras.get(MAX_ACTIVE_KEY).getAsInt());
				config.setMaxIdle(paras.get(MAX_IDLE_KEY).getAsInt());
				config.setMaxWait(paras.get(MAX_WAIT_KEY).getAsLong());
				config.setTestOnGet(paras.get(TEST_ON_BORROW_KEY)
						.getAsBoolean());
				config.setTestOnReturn(paras.get(TEST_ON_RETURN_KEY)
						.getAsBoolean());
				config.setGMPath(paras.get(GM_PATH_KEY).getAsString());
				this.gmService = new PooledGMService(config);

				this.imageSrcRoot = paras.get(IMAGE_SRC_ROOT).getAsString();
				this.imageNameSplit = paras.get(IMAGE_NAME_SPLIT).getAsString();
				this.imageTargetRoot = paras.get(IMAGE_TARGET_ROOT)
						.getAsString();
				this.extent = paras.get(EXTENT_KEY).getAsBoolean();
				this.quality = paras.get(QUALITY_KEY).getAsInt();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 使用sdk模式，初始化GMClient，通过mongoInfo信息，获取IDSSClient。
	 * @param parameter
	 * @param mongoInfo
	 */
	public GMClient(String parameter, String mongoInfo) {
		try {
			dc = DSSBaseFactory.getClient(mongoInfo);
			JsonObject paras = gson.fromJson(parameter, JsonObject.class);

			if (paras != null) {
				GMConnectionPoolConfig config = new GMConnectionPoolConfig();
				config.setMaxActive(paras.get(MAX_ACTIVE_KEY).getAsInt());
				config.setMaxIdle(paras.get(MAX_IDLE_KEY).getAsInt());
				config.setMaxWait(paras.get(MAX_WAIT_KEY).getAsLong());
				config.setTestOnGet(paras.get(TEST_ON_BORROW_KEY)
						.getAsBoolean());
				config.setTestOnReturn(paras.get(TEST_ON_RETURN_KEY)
						.getAsBoolean());
				config.setGMPath(paras.get(GM_PATH_KEY).getAsString());
				this.gmService = new PooledGMService(config);

				this.imageSrcRoot = paras.get(IMAGE_SRC_ROOT).getAsString();
				this.imageNameSplit = paras.get(IMAGE_NAME_SPLIT).getAsString();
				this.imageTargetRoot = paras.get(IMAGE_TARGET_ROOT)
						.getAsString();
				this.extent = paras.get(EXTENT_KEY).getAsBoolean();
				this.quality = paras.get(QUALITY_KEY).getAsInt();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	/**
	 * @param imageName 源图名
	 * @return
	 * @throws Exception
	 */
	public boolean isLocalImageExist(String imageName, String imageType)
			throws Exception {
		if (imageName == null)
			return false;
		String localPath = imageSrcRoot
				+ (imageSrcRoot.endsWith(File.separator) ? "" : File.separator)
				+ getFirstPath(imageName) + File.separator
				+ getSecondPath(imageName);
		forceMkdir(new File(localPath));
		log.info("------------------------localPath----------------------"
				+ localPath);
		return new File(localPath + File.separator + imageName + imageType)
				.exists();
	}

	public String getSourceImagePath(String imageName, String imageType)
			throws Exception {
		return (imageSrcRoot
				+ (imageSrcRoot.endsWith(File.separator) ? "" : File.separator)
				+ getFirstPath(imageName) + File.separator
				+ getSecondPath(imageName) + File.separator + imageName + imageType);
	}

	/**
	 * @param imageId 源图名
	 * @throws Exception
	 */
	public void getRomteImage(String imageId, String imageType)
			throws Exception {
		String imageName = imageSrcRoot
				+ (imageSrcRoot.endsWith(File.separator) ? "" : File.separator)
				+ getFirstPath(imageId) + File.separator
				+ getSecondPath(imageId) + File.separator + imageId + imageType;
		try {
			File file = new File(imageName);
			byte[] readin = dc.read(imageId);
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			fos.write(readin);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			log.error("get Image:" + imageName + " from dss error!", e);
		}
	}

	public String scaleImage(String uri, String imageName, int type,
			String imageSize, String imageType, boolean isExtent)
			throws Exception {
		long begin = System.currentTimeMillis();
		log.debug(uri + "----GraphicsImage----scaleImage---------begin");
		if (imageSize != null && imageSize.contains("X")) {
			imageSize = imageSize.replace("X", "x");
		}
		String targetPath = imageTargetRoot
				+ (imageTargetRoot.endsWith(File.separator) ? ""
						: File.separator) + getFirstPath(imageName)
				+ File.separator + getSecondPath(imageName) + File.separator
				+ imageName + imageNameSplit + imageSize + imageType;
		forceMkdir(new File(imageTargetRoot
				+ (imageTargetRoot.endsWith(File.separator) ? ""
						: File.separator) + getFirstPath(imageName)
				+ File.separator + getSecondPath(imageName)));
		GMConnection connection = null;
		try {
			String command = getCommand(imageName, type, imageSize, targetPath,
					imageType, isExtent, quality);
			log.debug(uri + "----GraphicsImage----scaleImage---------command:"
					+ command);
			connection = gmService.getConnection();
			connection.execute(command);
		} catch (Exception e) {
			log.error("scale image:" + targetPath, e);
			throw e;
		} finally {
			if (connection != null)
				connection.close();
		}
		log.debug(uri + "----GraphicsImage----scaleImage---------end 耗时"
				+ (System.currentTimeMillis() - begin));
		log.info(uri + "----GraphicsImage----targetPath---------" + targetPath);
		return targetPath;
	}

	/**
	 * 图片上增加水印
	 * 
	 * @param srcPath
	 * @param targetPath
	 * @throws Exception
	 */
	public void addImgText(String srcPath, String targetPath) throws Exception {
		// 暂时不实现
	}

	/**
	 * 删除本地文件，以节省空间
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void removeLocalSizedImage(String path) throws Exception {
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) {
				boolean result = false;
				int tryCount = 0;
				while (!result && tryCount++ < 3) {
					System.gc();
					result = file.delete();
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！");
		}
	}

	private String getCommand(String imageName, int type, String imageSize,
			String targetPath, String imageType, boolean isExtent, int quality)
			throws Exception {
		StringBuilder cmd = new StringBuilder();
		String width = imageSize.substring(0, imageSize.indexOf("x"));

		cmd.append(" convert ");
		cmd.append(" -scale ").append(imageSize.trim());
		if (!(extent || isExtent)) {
			if (Integer.valueOf(width) < 250)
				cmd.append("^ ");
		}
		// 去杂质，对于小图片质量百分百
		if (Integer.valueOf(width) < 250) {
			if (imageType.lastIndexOf(".jpg") >= 0) {
				cmd.append(" -strip -define jpeg:preserve-settings ");
				cmd.append(" -sharpen 0x1.2 ");
			}
			cmd.append(" -quality 100 ");
		} else {
			if (50 < quality && quality < 101) {
				cmd.append(" -quality ").append(quality);
			} else {
				cmd.append(" -quality 100 ");
			}
		}

		if (extent || isExtent) {
			cmd.append(" -background white ");
			cmd.append(" -gravity center ");
			cmd.append(" -extent ").append(imageSize);
		}

		cmd.append(" ")
				.append(imageSrcRoot)
				.append(imageSrcRoot.endsWith(File.separator) ? ""
						: File.separator).append(getFirstPath(imageName))
				.append(File.separator).append(getSecondPath(imageName))
				.append(File.separator).append(imageName).append(imageType);
		cmd.append(" ").append(targetPath);
		return cmd.toString();
	}

	/**
	 * 获得一级目录名称
	 *
	 * @param name
	 * @return
	 */
	private String getFirstPath(String path) {
		if (path == null || path.length() < 6)
			return null;
		return path.substring(0, 6);
	}

	/**
	 * 获得二级目录名称
	 *
	 * @param name
	 * @return
	 */
	private String getSecondPath(String path) {
		if (path == null || path.length() < 8)
			return null;
		return path.substring(6, 7);
	}

	/**
	 * 创建目录
	 *
	 * @param directory
	 * @throws IOException
	 */
	private void forceMkdir(File directory) throws IOException {
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				String message = "文件 " + directory + " 存在，不是目录。不能创建该目录。 ";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				if (!directory.isDirectory()) {
					String message = "不能创建该目录 " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	public void convertType(String src, String desc) throws Exception {
		long begin = System.currentTimeMillis();
		log.debug("----GraphicsImage----convertType---------begin");
		GMConnection connection = null;
		try {
			String command = getConvertTpyrCommand(src, desc);
			log.debug("----GraphicsImage----convertType---------command:"
					+ command);
			connection = gmService.getConnection();
			connection.execute(command);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		} finally {
			if (connection != null)
				connection.close();
		}
		log.debug("----GraphicsImage----convertType---------end 耗时"
				+ (System.currentTimeMillis() - begin));
	}

	public boolean judgeSize(String srcImage, int minWidth, int minHeight) {
		log.debug("----GraphicsImage----judge---------begin");
		if (minWidth <= 0 && minHeight <= 0)
			return true;
		GMConnection connection = null;
		try {
			String command = getSizeCommand(srcImage);
			log.debug("----GraphicsImage----judge---------command:" + command);
			connection = gmService.getConnection();
			String result = connection.execute(command);
			log.debug("----GraphicsImage----judge---------result:" + result);
			if (StringUtil.isBlank(result))
				return false;
			String[] size = result.split(",");
			if (null == size || size.length < 2) {
				return false;
			}
			int width = Integer.parseInt(size[0]);
			int height = Integer.parseInt(size[1]);
			log.debug("----GraphicsImage----judge---------width:" + width
					+ ",height:" + height);
			if (minWidth > 0 && width < minWidth) {
				return false;
			}
			if (minHeight > 0 && height < minHeight) {
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (GMServiceException e) {
					log.error("", e);
				}
		}
	}

	private String getConvertTpyrCommand(String src, String desc) {
		// 去杂质
		// 这里要判断一下图片类型
		if (desc.lastIndexOf(".jpg") >= 0) {
			return " convert" + " -strip -define jpeg:preserve-settings "
					+ " -quality 100" + " " + src + " " + desc;
		} else {
			// 按png处理
			return " convert" + " -quality 100 " + " " + src
					+ " +dither -depth 8 " + desc;
		}
	}

	private String getSizeCommand(String src) {
		return " identify  -format %w,%h" + " " + src;
	}

	public static void main(String[] args) {
		String result = "702,702";
		int minWidth = 750;
		int minHeight = 750;
		if (StringUtil.isBlank(result))
			System.out.println(false);
		String[] size = result.split(",");
		if (null == size || size.length < 2) {
			System.out.println(false);
		}
		int width = Integer.parseInt(size[0]);
		int height = Integer.parseInt(size[1]);
		if (minWidth > 0 && width < minWidth) {
			System.out.println(false);
		}
		if (minHeight > 0 && height < minHeight) {
			System.out.println(false);
		}
		System.out.println(true);
	}
}
