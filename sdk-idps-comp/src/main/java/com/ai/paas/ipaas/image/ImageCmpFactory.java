package com.ai.paas.ipaas.image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ai.paas.ipaas.image.impl.ImageClientImpl;
import com.ai.paas.ipaas.util.Assert;

public class ImageCmpFactory {
	private static Map<String, IImageClient> imageClients = new ConcurrentHashMap<String, IImageClient>();

	private ImageCmpFactory(String imageUrl) {
	}

	/**
	 * 获取图片客户端
	 * 
	 * @param interUrl
	 *            图片对外服务地址，可以是nginx地址或者域名加地址 http://xxx.xxx.xxx.xxx:port/image
	 * @param intraUrl
	 * @return
	 * @throws Exception
	 */
	public static IImageClient getClient(String interUrl, String intraUrl)
			throws Exception {
		IImageClient iImageClient = null;
		Assert.notNull(interUrl, "the internet address can not null!");
		Assert.notNull(intraUrl, "the intranet address can not null!");
		if (interUrl.endsWith("/"))
			interUrl = interUrl.substring(0, interUrl.lastIndexOf("/"));
		if (intraUrl.endsWith("/"))
			intraUrl = intraUrl.substring(0, intraUrl.lastIndexOf("/"));

		// 判断一下是否有
		if (null != imageClients.get(interUrl))
			return imageClients.get(interUrl);
		iImageClient = new ImageClientImpl(null,null, null, intraUrl,
				interUrl);
		imageClients.put("ImageCmpClient", iImageClient);
		return iImageClient;
	}
}
