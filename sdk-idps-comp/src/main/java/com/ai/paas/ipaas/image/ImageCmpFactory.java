package com.ai.paas.ipaas.image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ai.paas.ipaas.image.impl.ImageClientImpl;
import com.ai.paas.ipaas.utils.IdpsContant;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ImageCmpFactory {
	private static Map<String, IImageClient> imageClients = new ConcurrentHashMap<String, IImageClient>();
	private String imageUrl = null;
	private String mongoInfo = null;
	
	/**
	 * @param imageUrl "http://10.1.235.199:8086/iPaas-IDPS"
	 * @param mongoInfo 
	 *  {"mongoServer":"10.1.xxx.xxx:37017","database":"image","userName":"idps","password":"idps"}
	 */
	public ImageCmpFactory(String imageUrl, String mongoInfo) {
		this.imageUrl = imageUrl;
		this.mongoInfo = mongoInfo;
	}

	public IImageClient getClient() throws Exception {
		JsonParser parser = new JsonParser();
		JsonElement je = parser.parse(mongoInfo);
		JsonObject mongoObj = je.getAsJsonObject();
		String mongoServer = mongoObj.get("mongoServer").getAsString();
		
		IImageClient iImageClient = null;
		iImageClient = new ImageClientImpl(IdpsContant.NO_NEED_AUTH, "", "", "", mongoInfo, imageUrl, imageUrl);
		imageClients.put(mongoServer, iImageClient);
		
		return iImageClient;
	}

}
