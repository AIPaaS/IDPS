package com.ai.paas.ipaas.image;
//接口定义

import java.util.List;
import java.util.Map;


public interface IImageClient {



    
    
    String upLoadImage(byte[] image,String name);
    
    
    String getImageServer();
    
    
    String getImageServerForIn();
    
    String getImageUrl(String imageId,String imageType);
    
    String getUpImageUrl();
    
    
}