package com.ai.paas.ipaas.image.impl;
//接口定义

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.utils.HttpUtil;
import com.google.gson.Gson;


public class ImageClientImpl implements IImageClient{

	private Logger log =  LoggerFactory.getLogger(ImageClientImpl.class);
	private String userId;
	private String serviceId;
	private String imageUrl;
	private String imageUrlOut;
	private Gson gson = new Gson();
	
	
	public ImageClientImpl(String userId,String serviceId,String imageUrl,String imageUrlOut) {
		this.userId = userId;
		this.serviceId = serviceId;
		this.imageUrl = imageUrl;
		this.imageUrlOut = imageUrlOut;
		
	}
	
	
	public String upLoadImage(byte[] image,String name){
		
		if(image == null)
			return null;
		if(image.length>5*1024*1024){
			log.error("上传的图片大于5M!");
			return null;
		}
		String id = null;
		String upUrl = getUpImageUrl();
		if(upUrl == null || upUrl.length() == 0){
			log.error("请检查配置,上传图片的服务器地址。");
			return null;
		}
		upUrl= upUrl+"?userId="+userId+"&serviceId="+serviceId;
		String result = HttpUtil.upImage(upUrl, image,name);
		Map<String,String> json =new HashMap<String,String>();
		json =  gson.fromJson(result, json.getClass());
		if("success".equals(json.get("result"))){
			id = json.get("id");
		}
		return id;
	}

	
	public String getImageServer(){
		return imageUrlOut;
	}
	
	public  String getImageServerForIn(){
		
		return imageUrl;
	}
	
	public InputStream downloadImage(String imageId,String imageType,String imageScale){
		
		String downloadUrl = "";
		if(StringUtils.isEmpty(imageScale)){
			downloadUrl = imageUrl+"/image/"+imageId+imageType+"?userId="+userId+"&serviceId="+serviceId;
		}else{
			downloadUrl = imageUrl+"/image/"+imageId+"_"+imageScale+imageType+"?userId="+userId+"&serviceId="+serviceId;
		}
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(downloadUrl);
		
		InputStream in  = null ;
		try {  
            client.executeMethod(httpGet);  
              
           in = httpGet.getResponseBodyAsStream();  
              
        }catch (Exception e){  
            e.printStackTrace();  
        }finally{  
            httpGet.releaseConnection();  
        }  
        System.out.println("download, success!!");  
		
		return in;
	}
	
	
	public String getImageUrl(String imageId,String imageType){
		
		return imageUrl+"/image/"+imageId+imageType+"?userId="+userId+"&serviceId="+serviceId;
	}
	
	public  String getUpImageUrl(){
		return imageUrl+"/upImage";
	}
}