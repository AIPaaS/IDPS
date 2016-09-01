package com.ai.paas.ipaas.image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.paas.ipaas.image.impl.ImageClientImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ImageCmpFactory {
	private static transient final Logger log = LoggerFactory.getLogger(ImageCmpFactory.class);
	private static Map<String, IImageClient> imageClients = new ConcurrentHashMap<String, IImageClient>();
	private static String imageUrl = null;
	private static String mongoInfo = null;
	
	/**
	 * @param imageUrl
	 * 	"http://10.1.235.199:8086/iPaas-IDPS"
	 * @param mongoInfo
	 *  {"mongoServer":"10.1.xxx.xxx:37017;10.1.xxx.xxx:37017",
	 *            "database":"image","userName":"sa","password":"sa"}
	 * @param needAuth
	 *   fasle:no need uac, true:invoke uac.s
	 */
	public ImageCmpFactory(String imageUrl, String mongoInfo) {
		this.imageUrl = imageUrl;
		this.mongoInfo = mongoInfo;
	}

	@SuppressWarnings("unchecked")
	public IImageClient getClient() throws Exception {
		IImageClient iImageClient = null;
		
		JsonParser parser = new JsonParser();
		JsonElement je = parser.parse(mongoInfo);
		JsonObject mongoObj = je.getAsJsonObject();
		String mongoServer = mongoObj.get("mongoServer").getAsString();
		
		iImageClient = new ImageClientImpl(false, "", "", "", mongoInfo, imageUrl, imageUrl);
		imageClients.put(mongoServer, iImageClient);
		
		return iImageClient;
	}

}
