package com.ai.paas.ipaas.image.impl;

//接口定义

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.utils.HttpUtil;
import com.google.gson.Gson;

public class ImageClientImpl implements IImageClient {

	private static transient Logger log = LoggerFactory
			.getLogger(ImageClientImpl.class);
	private String userId;
	private String serviceId;
	private String imageUrl;
	private String imageUrlInter;
	private Gson gson = new Gson();

	public ImageClientImpl(String userId, String serviceId, String imageUrl,
			String imageUrlInter) {
		this.userId = userId;
		this.serviceId = serviceId;
		this.imageUrl = imageUrl;
		this.imageUrlInter = imageUrlInter;

	}

	@SuppressWarnings("unchecked")
	public String upLoadImage(byte[] image, String name) {

		if (image == null)
			return null;
		if (image.length > 10 * 1024 * 1024) {
			log.error("upload image size great than 10M of " + name);
			return null;
		}
		String id = null;
		String upUrl = getImageUploadUrl();
		if (upUrl == null || upUrl.length() == 0) {
			log.error("no upload url, pls. check service configration.");
			return null;
		}
		upUrl = upUrl + "?userId=" + userId + "&serviceId=" + serviceId;
		String result = HttpUtil.upImage(upUrl, image, name);
		Map<String, String> json = new HashMap<String, String>();
		json = gson.fromJson(result, Map.class);
		if ("success".equals(json.get("result"))) {
			id = json.get("id");
		}
		return id;
	}

	public String getImgServerInterAddr() {
		return imageUrlInter;
	}

	public String getImgServerIntraAddr() {

		return imageUrl;
	}

	public InputStream getImageStream(String imageId, String imageType,
			String imageScale) {

		String downloadUrl = "";

		if (StringUtils.isEmpty(imageScale)) {
			downloadUrl = imageUrl + "/image/" + imageId + imageType
					+ "?userId=" + userId + "&serviceId=" + serviceId;
		} else {
			downloadUrl = imageUrl + "/image/" + imageId + "_" + imageScale
					+ imageType + "?userId=" + userId + "&serviceId="
					+ serviceId;
		}
		log.info("Start to download " + downloadUrl);
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(downloadUrl);

		InputStream in = null;
		try {
			client.executeMethod(httpGet);
			if (200 == httpGet.getStatusCode()) {
				in = httpGet.getResponseBodyAsStream();
				log.info("Successfully download " + downloadUrl);
			}
		} catch (Exception e) {
			log.error("download " + imageId + "." + imageType + ", scale: "
					+ imageScale, e);
		} finally {
			httpGet.releaseConnection();
		}
		return in;
	}

	public boolean deleteImage(String imageId) {
		String deleteUrl = imageUrl + "/deleteImage?imageId=" + imageId
				+ "&userId" + userId + "&serviceId=" + serviceId;
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(deleteUrl);
		log.info("Start to delete " + deleteUrl);

		try {
			client.executeMethod(httpGet);

			if (200 == httpGet.getStatusCode()) {
				log.info("Successfully delete " + deleteUrl);
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			log.error("delete  " + deleteUrl, e);
			return false;
		} finally {
			httpGet.releaseConnection();
		}

	}

	private String imageTypeFormat(String imageType) {
		if (imageType !=null && imageType.startsWith(".")==false) {
			imageType = "." + imageType;
		}
		switch (imageType) {
		case ".JPG":
			imageType = ".jpg";
			break;
		case ".PNG":
			imageType = ".png";
			break;
		default:
		}
		
		return imageType;
	}
	
	public String getImageUrl(String imageId, String imageType) {
		imageType = imageTypeFormat(imageType);
		return imageUrlInter + "/image/" + imageId + imageType + "?userId="
				+ userId + "&serviceId=" + serviceId;
	}
	
	public String getImageUrl(String imageId, String imageType, String imageScale) {
		imageType = imageTypeFormat(imageType);
		if (imageScale != null && imageScale.contains("X")) {
			imageScale = imageScale.replace("X","x");
		}
		return imageUrlInter + "/image/" + imageId +"_"+ imageScale + imageType + "?userId="
				+ userId + "&serviceId=" + serviceId;
	}

	public String getImageUploadUrl() {
		return imageUrl + "/uploadImage";
	}

	@Override
	public byte[] getImage(String imageId, String imageType, String imageScale) {
		byte[] bytes = null;

		String downloadUrl = "";

		if (StringUtils.isEmpty(imageScale)) {
			downloadUrl = imageUrl + "/image/" + imageId + imageType
					+ "?userId=" + userId + "&serviceId=" + serviceId;
		} else {
			downloadUrl = imageUrl + "/image/" + imageId + "_" + imageScale
					+ imageType + "?userId=" + userId + "&serviceId="
					+ serviceId;
		}
		log.info("Start to download " + downloadUrl);
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(downloadUrl);

		try {
			client.executeMethod(httpGet);
			if (200 == httpGet.getStatusCode()) {
				bytes = httpGet.getResponseBody();
				log.info("Successfully download " + downloadUrl);
			}
		} catch (Exception e) {
			log.error("download " + imageId + "." + imageType + ", scale: "
					+ imageScale, e);
		} finally {
			httpGet.releaseConnection();
		}
		return bytes;
	}
}