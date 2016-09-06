package com.ai.paas.ipaas.image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ai.paas.ipaas.image.impl.ImageClientImpl;

public class ImageCmpFactory {
	private String imageUrl = null;
	private static Map<String, IImageClient> imageClients = new ConcurrentHashMap<String, IImageClient>();
	
	/**
	 * @param imageUrl "http://10.1.235.199:8086/iPaas-IDPS"
	 */
	public ImageCmpFactory(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public IImageClient getClient() throws Exception {
		IImageClient iImageClient = null;
		iImageClient = new ImageClientImpl("false", "", "", "", imageUrl, imageUrl);
		imageClients.put("ImageCmpClient", iImageClient);
		return iImageClient;
	}
}
