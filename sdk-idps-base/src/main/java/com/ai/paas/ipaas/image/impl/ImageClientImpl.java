package com.ai.paas.ipaas.image.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.paas.GeneralRuntimeException;
import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.image.IdpsContant;
import com.ai.paas.ipaas.image.exception.ImageSizeIllegalException;
import com.ai.paas.ipaas.image.utils.ImageUtil;
import com.ai.paas.util.CiperUtil;
import com.ai.paas.util.JsonUtil;
import com.ai.paas.util.StringUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageClientImpl implements IImageClient {
    private static Logger log = LoggerFactory.getLogger(ImageClientImpl.class);

    private String pId;
    private String srvId;
    private String srvPwd;
    private String imageUrl;
    private String imageUrlInter;

    public ImageClientImpl(String pId, String srvId, String srvPwd, String imageUrl, String imageUrlInter) {
        this.pId = pId;
        this.srvId = srvId;
        this.srvPwd = srvPwd;
        this.imageUrl = imageUrl;
        this.imageUrlInter = imageUrlInter;
    }

    public String upLoadImage(byte[] image, String name) {
        return upLoadImage(image, name, 0, 0);
    }

    public String getImgServerInterAddr() {
        return imageUrlInter;
    }

    public String getImgServerIntraAddr() {
        return imageUrl;
    }

    public InputStream getImageStream(String imageId, String imageType, String imageScale) {
        String downloadUrl = "";
        if (StringUtils.isEmpty(imageScale)) {
            downloadUrl = imageUrl + "/image/" + imageId + imageType;
        } else {
            downloadUrl = imageUrl + "/image/" + imageId + "_" + imageScale + imageType;
        }

        log.info("Start to download {}", downloadUrl);

        InputStream in = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get() // 默认get方式 可省略不写
                    .url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                in = response.body().byteStream();
                log.info("Successfully download {}", downloadUrl);
            }
        } catch (Exception e) {
            log.error("download " + imageId + "." + imageType + ", scale: " + imageScale, e);
        }
        return in;
    }

    public boolean deleteImage(String imageId) {
        String deleteUrl = imageUrl + "/deleteImage?imageId=" + imageId;
        return ImageUtil.delImage(deleteUrl, createToken());
    }

    private String imageTypeFormat(String imageType) {
        if (null == imageType)
            return null;
        if (!imageType.startsWith(".")) {
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
        return imageUrlInter + "/image/" + imageId + imageType;
    }

    public String getImageUrl(String imageId, String imageType, String imageScale) {
        imageType = imageTypeFormat(imageType);
        if (imageScale != null && imageScale.contains("X")) {
            imageScale = imageScale.replace("X", "x");
        }
        return imageUrlInter + "/image/" + imageId + "_" + imageScale + imageType;
    }

    public String getImageUploadUrl() {
        return imageUrl + "/uploadImage";
    }

    @Override
    public byte[] getImage(String imageId, String imageType, String imageScale) {
        String downloadUrl = "";
        if (StringUtils.isEmpty(imageScale)) {
            downloadUrl = imageUrl + "/image/" + imageId + imageType;
        } else {
            downloadUrl = imageUrl + "/image/" + imageId + "_" + imageScale + imageType;
        }

        return ImageUtil.getImage(downloadUrl);
    }

    private String createToken() {
        String token = null;
        if (StringUtil.isBlank(pId))
            return token;
        Map<String, String> json = new HashMap<>();
        json.put("pid", this.pId);
        json.put("srvId", this.srvId);
        json.put("srvPwd", this.srvPwd);
        String data = JsonUtil.toJson(json);
        token = CiperUtil.encrypt(IdpsContant.IDPS_SEC_KEY, data);
        return token;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String upLoadImage(byte[] image, String name, int minWidth, int minHeight) {

        if (image == null)
            return null;
        if (image.length > 10 * 1024 * 1024) {
            log.error("upload image size great than 10M of {}", name);
            return null;
        }
        String id = null;
        String upUrl = getImageUploadUrl();
        if (upUrl == null || upUrl.length() == 0) {
            log.error("no upload url, pls. check service configration.");
            return null;
        }

        // 上传和删除要加安全验证 ，先简单实现吧，在头上放置用户的pid和服务id，及服务密码的sha1串，在服务端进行验证
        String result = ImageUtil.upImage(upUrl, image, name, minWidth, minHeight, createToken());
        Map<String, String> json = null;
        json = JsonUtil.fromJson(result, Map.class);
        if (null != json && null != json.get("result") && "success".equals(json.get("result"))) {
            id = json.get("id");
        } else {
            // 这里进行异常的处理
            if (null != json && null != json.get("exception")) {
                String exception = json.get("exception");
                if (ImageSizeIllegalException.class.getSimpleName().equalsIgnoreCase(exception)) {
                    throw new ImageSizeIllegalException(json.get("message"));
                }
            }
            log.error("result:{},json:{}", result, json);
            throw new GeneralRuntimeException(result);
        }

        return id;
    }

}