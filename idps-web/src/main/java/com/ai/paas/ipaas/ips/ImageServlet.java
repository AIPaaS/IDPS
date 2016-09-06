package com.ai.paas.ipaas.ips;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.base.DSSBaseFactory;
import com.ai.paas.ipaas.dss.base.interfaces.IDSSClient;
import com.ai.paas.ipaas.utils.AuthUtil;
import com.ai.paas.ipaas.utils.ImageUtil;
import com.ai.paas.ipaas.utils.SubAuthDescriptor;

public class ImageServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ImageServlet.class);
	private static final long serialVersionUID = 1594325791647123L;
	private DateFormat df = getDateFormat();
	private SubAuthDescriptor ad = null;
	private IDSSClient dc = null;
	private ImageUtil util;

	@Override
	public void init() throws ServletException {
		ad = AuthUtil.getAuthInfo();
		if (null == ad) {
			throw new ServletException(
					"Can not get auth info, pls. set in ENV or -DAUTH_URL=XXX -DAUTH_USER_PID -DAUTH_SRV_PWD -DAUTH_SRV_ID");
		}
		try {
			if (null != ad.getIsNeedAuth() && "true".equals(ad.getIsNeedAuth())) {
				dc = DSSFactory.getClient(ad);
			}else{
				dc = DSSBaseFactory.getClient(ad.getMongoInfo());
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		util = new ImageUtil(ad);
		super.init();
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {

		long begin = System.currentTimeMillis();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String uri = request.getRequestURI();

		log.debug(uri + "--service------------------");
		String imageType = getImageType(uri);
		if (!util.isSupported(imageType)) {
			log.error(uri + "--service------------------资源格式不支持" + imageType);
			return;
		}
		String imageName = getImageName(uri);
		// 还是要判断是佛304模式
		if (isCachedByBrowser(request, imageName)) {
			// 直接返回
			response.setStatus(304);
			return;
		} else {
			// 设置缓存10年
			setImageCacheHeader(response, imageName);
		}

		if (util.getGMMode()) {
			String imageSize = null;
			boolean isExtent = false;
			if (!isSourceImage(uri)) {
				imageSize = getImageSize(uri);
				if (imageSize.endsWith("!")) {
					isExtent = true;
					imageSize = imageSize.replace("!", "");
				}

			} else {
				if (imageName.endsWith("!")) {
					isExtent = true;
					imageName = imageName.replace("!", "");
				}
			}
			// 得到缩略图
			log.debug(uri + "-----------imageName:" + imageName
					+ "   ----------imageSize:" + imageSize);
			String imagePath = "";
			try {
				imagePath = util.getScaleImage(uri, imageName, imageSize,
						imageType, isExtent);
			} catch (Exception e) {
				log.error("uri:" + uri + ",imageName=" + imageName, e);
				imagePath = util.getReservePath() + "error.jpg";
				log.error(uri + "--异常时,返回图片路径------------------" + imagePath);
			}
			// 返回图片字节流
			File file = new File(imagePath);
			if (file.exists()) {
				InputStream in = null;
				ServletOutputStream outStream = null;
				try {
					in = new FileInputStream(file);
					if (in != null)
						response.setIntHeader("Content-Length", in.available());
					// 奇怪这里为什么要503呢
					response.setStatus(200);
					outStream = response.getOutputStream();
					byte[] data = new byte[2048];
					int count = -1;
					while ((count = in.read(data, 0, 2048)) != -1)
						outStream.write(data, 0, count);
					data = null;
					outStream.flush();
					log.debug(uri + "--return------------------ok");
				} catch (Exception e) {
					log.error("uri:" + uri + ",imagePath=" + imagePath, e);
				} finally {
					try {
						if (outStream != null) {
							outStream.close();
						}
					} catch (Exception e) {
						log.error("", e);
					}
					if (in != null) {
						in.close();
					}
				}
			}
		} else {
			// 不需要GM动态产生缩略图
			responseMongoImage(response, uri);
		}
		log.debug(uri + "-------------------:共耗时"
				+ (System.currentTimeMillis() - begin) + "ms");
	}

	/**
	 * 是否已被浏览器缓存，服务端没有修改，只需要返回304
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean isCachedByBrowser(HttpServletRequest request,
			String imageName) {
		// 是否已经被IE缓存，没有修改
		boolean isCached = false;
		String modifiedSince = request.getHeader("If-Modified-Since");
		if (df == null)
			df = getDateFormat();
		long update = getUpdateTime(imageName);
		String lastModified = df.format(new Date(update));
		if (modifiedSince != null) {
			Date modifiedDate = null;
			Date sinceDate = null;
			try {
				modifiedDate = df.parse(lastModified);
				sinceDate = df.parse(modifiedSince);
			} catch (ParseException e) {
				log.error("", e);
			}
			if (modifiedDate.compareTo(sinceDate) <= 0) {
				isCached = true;
			}
		}

		return isCached;
	}

	private void setImageCacheHeader(HttpServletResponse response,
			String imageName) {
		long update = getUpdateTime(imageName);
		String lastModified = df.format(new Date(update));
		response.setContentType("image/jpeg");
		long maxAge = 10L * 365L * 24L * 60L * 60L; // ten years, in seconds
		response.setHeader("Cache-Control", "max-age=" + maxAge);
		response.setHeader("Last-Modified", lastModified);
		response.setDateHeader("Expires", update + maxAge * 1000L);
	}

	private DateFormat getDateFormat() {
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
				Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df;
	}

	private long getUpdateTime(String imageName) {
		Date updateTime = null;
		try {
			updateTime = dc.getLastUpdateTime(imageName);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return updateTime != null ? updateTime.getTime() : 1412931179491l;
	}

	/**
	 * 判断是否是源图
	 * 
	 * @param uri
	 * @return
	 */
	private boolean isSourceImage(String uri) {
		return !getName(uri).contains("_");
	}

	private void responseMongoImage(HttpServletResponse response, String uri) {

		// 直接返回mongoDB中的图（源图＋缩略图 都先存储在mongoDB）
		String name = getImageName(uri);
		log.debug(uri + "--service--------------不使用GM动态生成缩略图----");
		byte[] images = dc.read(name);
		// 返回图片字节流
		if (images != null && images.length > 0) {
			ServletOutputStream outStream = null;
			try {
				response.setIntHeader("Content-Length", images.length);
				outStream = response.getOutputStream();
				outStream.write(images, 0, images.length);
				outStream.flush();
				images = null;
				log.debug(uri + "--return------------------ok");
			} catch (Exception e) {
				log.error("", e);
			} finally {
				try {
					if (outStream != null) {
						outStream.close();
					}
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

	private String getImageSize(String uri) {
		if (uri == null)
			return null;
		String image = null;
		if (uri.contains("?")) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		if (uri.contains("/")) {
			String[] urls = uri.split("/");
			image = urls[urls.length - 1];
		} else {
			image = uri;
		}
		if (image != null) {
			if (image.contains("_")) {
				image = image.substring(image.indexOf("_") + 1,
						image.indexOf("."));
			}
		}
		return image;
	}

	private String getImageName(String uri) {
		if (uri == null)
			return null;
		String image = null;
		if (uri.contains("?")) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		if (uri.contains("/")) {
			String[] urls = uri.split("/");
			image = urls[urls.length - 1];
		} else {
			image = uri;
		}
		if (image != null) {
			if (image.contains("_")) {
				image = image.substring(0, image.indexOf("_"));
			} else {
				image = image.substring(0, image.indexOf("."));
			}

		}
		return image;
	}

	private String getName(String uri) {
		if (uri == null)
			return null;
		String image = null;
		if (uri.contains("?")) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		if (uri.contains("/")) {
			String[] urls = uri.split("/");
			image = urls[urls.length - 1];
		} else {
			image = uri;
		}
		return image;
	}

	private String getImageType(String uri) {
		if (uri == null)
			return null;
		String image = null;
		if (uri.contains("?")) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		if (uri.contains("/")) {
			String[] urls = uri.split("/");
			image = urls[urls.length - 1];
		} else {
			image = uri;
		}
		if (image != null) {
			if (image.contains(".")) {
				image = image.substring(image.indexOf("."));
			} else {
				image = null;
			}
		}
		return image;
	}

}
