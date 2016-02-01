package com.ai.paas.ipaas.ips.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;
import org.gm4java.engine.GMConnection;
import org.gm4java.engine.GMService;
import org.gm4java.engine.support.GMConnectionPoolConfig;
import org.gm4java.engine.support.PooledGMService;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.interfaces.IDSSClient;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.google.gson.Gson;


public class GMClient {

    private static final Logger log = Logger.getLogger(GMClient.class);

    private GMService gMService;

    private static final String GM_PATH_KEY = "gmPath";
    private static final String MAX_ACTIVE_KEY = "maxActive";
    private static final String MAX_WAIT_KEY = "maxWait";
    private static final String MAX_IDLE_KEY = "maxIdle";
    private static final String TEST_ON_BORROW_KEY = "testOnBorrow";
    private static final String TEST_ON_RETURN_KEY = "testOnReturn";
    private static final String IMAGE_ROOT = "imageRoot";
    private static final String IMAGE_ROOT_NEW = "imageRootNew";
    private static final String IMAGE_NAME_SPLIT = "imageNameSplit";
    private static final String EXTENT_KEY = "extent";
    private static final String QUALITY_KEY = "quality";
    private static final String AUTH_URL = "authUrl";

    private static final String REDISKEYPREFIX_KEY = "redisKeyPrefix";
    private static final String VARNISHSERVER_KEY = "varnishServer";
    private static final String COMMANDSIZE_KEY = "commandSize";
    private static final String PRIMARYPARAM_KEY = "primaryParam";


    //本地保存图片的路径 源图
    private String imageRoot = null;
    //图片名分隔符 _
    private String imageNameSplit = null;
    //本地保存图片的路径 缩略图
    private String imageRootNew = null;
    //是否补白尺寸图
    private boolean extent = true;
    //图片质量
    private int quality = 90;

    //图片缓存入redis，前缀
    private String redisKeyPrefix;
    //varnish服务器ip、port
    private String varnishServer;
    //varnish服务器ip、port
    private List	  varnishServers;
    //与varnish一次连接，清除缓存url的个数
    private int commandSize;
    //url中关键参数名称 如：productId
    private String primaryParam;
    private String userPid;
	private String servicePwd;
	private String serviceId; 
	private String authUrl;
    private Gson gson = new Gson();
    
    private static AuthDescriptor ad = null;
	private static IDSSClient dc = null;

    public GMClient(String parameter,String userPid,String servicePwd,String serviceId) {
        try {
        	this.userPid= userPid;
    		this.servicePwd = servicePwd;
    		this.serviceId = serviceId;
            Map<String,String> json =new HashMap<String,String>();
            json = gson.fromJson(parameter, json.getClass());
            
            
            if (json != null) {
                GMConnectionPoolConfig config = new GMConnectionPoolConfig();
                config.setMaxActive(Integer.parseInt(json.get(MAX_ACTIVE_KEY)));
                config.setMaxIdle(Integer.parseInt(json.get(MAX_IDLE_KEY)));
                config.setMaxWait(Long.parseLong(json.get(MAX_WAIT_KEY)));
                config.setTestOnGet(Boolean.parseBoolean(json.get(TEST_ON_BORROW_KEY)));
                config.setTestOnReturn(Boolean.parseBoolean(json.get(TEST_ON_RETURN_KEY)));
                config.setGMPath(json.get(GM_PATH_KEY));
                this.gMService = new PooledGMService(config);

                this.imageRoot = json.get(IMAGE_ROOT);
                this.imageNameSplit = json.get(IMAGE_NAME_SPLIT);
                this.imageRootNew = json.get(IMAGE_ROOT_NEW);
                this.extent =Boolean.parseBoolean(json.get(EXTENT_KEY));
                this.quality = Integer.parseInt(json.get(QUALITY_KEY));
                this.authUrl = json.get(AUTH_URL);
                this.redisKeyPrefix = json.get(REDISKEYPREFIX_KEY);
        
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
    public boolean localImageExist(String imageName, String imageType) throws Exception {
        if (imageName == null)
            return false;
        String localPath = imageRoot + (imageRoot.endsWith(File.separator) ? "" : File.separator) + getFirstPath(imageName) + File.separator + getSecondPath(imageName);
        forceMkdir(new File(localPath));
        log.debug("------------------------localPath----------------------" + localPath);
        return new File(localPath + File.separator + imageName + imageType).exists();
    }

    public String getSourceImagePath(String imageName, String imageType) throws Exception {
        return (imageRoot + (imageRoot.endsWith(File.separator) ? "" : File.separator) + getFirstPath(imageName) + File.separator + getSecondPath(imageName) + File.separator + imageName + imageType);
    }

    /**
     * @param imageId 源图名
     * @throws Exception
     */
    public void getRomteImage(String imageId, String imageType) throws Exception {
    	String imageName = imageRoot + (imageRoot.endsWith(File.separator) ? "" : File.separator) + getFirstPath(imageId) + File.separator + getSecondPath(imageId) + File.separator + imageId + imageType;
		try {
			File file = new File(imageName);
			ad =  new AuthDescriptor(authUrl, userPid, servicePwd,serviceId);
			dc = DSSFactory.getClient(ad);
			byte[] readin = dc.read(imageId);
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			fos.write(readin);
			fos.flush();
			fos.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    public String scaleImage(String uri, String imageName, int type,
                             String imageSize, String imageType, boolean isExtent) throws Exception {
        long begin = System.currentTimeMillis();
        log.debug(uri + "----GraphicsImage----scaleImage---------begin");
        String newPath = imageRootNew + (imageRootNew.endsWith(File.separator) ? "" : File.separator) + getFirstPath(imageName) + File.separator + getSecondPath(imageName) + File.separator + imageName + imageNameSplit + imageSize + imageType;
        forceMkdir(new File(imageRootNew + (imageRootNew.endsWith(File.separator) ? "" : File.separator) + getFirstPath(imageName) + File.separator + getSecondPath(imageName)));
        GMConnection connection = null;
        try {
            String command = getCommand(imageName, type, imageSize, newPath, imageType, isExtent);
            log.debug(uri + "----GraphicsImage----scaleImage---------command:" + command);
            connection = gMService.getConnection();
            connection.execute(command);
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if (connection != null)
                connection.close();
        }
        log.debug(uri + "----GraphicsImage----scaleImage---------end 耗时" + (System.currentTimeMillis() - begin));
        log.debug(uri + "----GraphicsImage----newPath---------" + newPath);
        return newPath;
    }

    public void addImgText(String srcPath, String newPath) throws Exception {
        //暂时不实现

    }


    public void removeLocalCutedImage(String path) throws Exception {
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

//    public String saveToRomte(File image, String imageName, String imageType) throws Exception {
//        if (image != null && imageName != null && imageType != null) {
//            FileInputStream input = null;
//            try {
//                input = new FileInputStream(image);
//                byte[] datas = new byte[input.available()];
//                input.read(datas);
//                return MongoFileUtil.saveFile(datas, imageName, imageType);
//            } catch (Exception e) {
//                log.error("保存图片失败", e);
//                throw e;
//            } finally {
//                if (input != null)
//                    input.close();
//            }
//        } else {
//            throw new Exception("参数非法，要求都不为空！");
//        }
//    }

//    public boolean removeCacheHtml(String productId) throws Exception {
//        if (productId == null || "".equals(productId)) {
//            log.error("入参商品ID为空");
//            return false;
//        }
//
//        Set<String> redisKeys = getRedisKey(productId);
//
//        //清除varnish缓存
//        Set<String> varnishUrlKeys = getVarnishUrlKey(redisKeys, productId);
//        log.debug("----------------varnishUrlKeys缓存标识-----------" + varnishUrlKeys);
//        if (varnishUrlKeys != null && varnishUrlKeys.size() > 0)
//            delVarnishItem(varnishUrlKeys);
//
//        //清除redis缓存
//        log.debug("----------------redisKeys缓存标识-----------" + redisKeys);
//        if (redisKeys != null && redisKeys.size() > 0) {
//            for (String key : redisKeys)
//                CacheUtil.delItem(key);
//        }
//
//        return true;
//    }


    /**
     * 每次与varnish管理端口建立连接，执行删除缓存
     *
     * @param ip
     * @param port
     * @param varnishUrlKeys
     * @param i
     */
    private void delVarnishItemPre(String ip, int port, String[] varnishUrlKeys, int i) {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 5000);
            socket.setSendBufferSize(1000);
            socket.setSoTimeout(5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            for (int k = 0; k < commandSize && k < varnishUrlKeys.length; k++) {
                String url = varnishUrlKeys[commandSize * (i - 1) + k];
                String baseCMD = "ban";
                String command = baseCMD + ".url " + url;
                writer.println(command);
                writer.flush();
                String result;
                while ((result = reader.readLine()) != null) {
                    result = result.trim();
                    log.debug("----command------" + command + "---------------------" + result);
                    if (result.equals("200 0")) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                log.error("", e);
            }
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }

    }

    private Set<String> getVarnishUrlKey(Set<String> keys, String productId) {
        Set<String> res = new HashSet<String>();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                //TODO rediskey-->url  带上产品ID  产品ID必须
                key = key.replace(redisKeyPrefix, "^");
                if (key.contains(".._" + productId)) {
                    if (key.indexOf(".._" + productId) == key.indexOf(".._"))
                        key = key.substring(0, key.indexOf(".._" + productId)) + "\\?" + primaryParam + "=" + productId;
                    else
                        key = key.substring(0, key.indexOf(".._" + productId)) + primaryParam + "=" + productId;
                }
                if (key.contains(".._"))
                    key = key.replace(".._", ".*");
                key += ".*$";
                res.add(key);
            }
        }
        return res;
    }

//    private Set<String> getRedisKey(String productId) {
//        return CacheUtil.getSetForStatic(redisKeyPrefix + productId);
//    }

    private String getCommand(String imageName, int type, String imageSize, String newPath, String imageType, boolean isExtent) throws Exception {
        StringBuilder cmdStr = new StringBuilder();
        String width = imageSize.substring(0, imageSize.indexOf("x"));

        cmdStr.append(" convert ");
        cmdStr.append(" -scale ").append(imageSize.trim());
        if (!(extent || isExtent)) {
            if (Integer.valueOf(width) < 250) cmdStr.append("^ ");
        }
        //去杂质
        if (Integer.valueOf(width) < 250) {
            cmdStr.append(" -strip -define jpeg:preserve-settings ");
            cmdStr.append(" -sharpen 0x1.2 ");
            cmdStr.append(" -quality 100 ");
        }

        if (extent || isExtent) {
            cmdStr.append(" -background white ");
            cmdStr.append(" -gravity center ");
            cmdStr.append(" -extent ").append(imageSize);
        }

		/*if (0 < quality && quality < 101) {
            cmdStr.append(" -quality ").append(quality);
		}*/

        cmdStr.append(" ").
                append(imageRoot).append(imageRoot.endsWith(File.separator) ? "" : File.separator).
                append(getFirstPath(imageName)).append(File.separator).
                append(getSecondPath(imageName)).append(File.separator).
                append(imageName).append(imageType);
        cmdStr.append(" ").append(newPath);
        return cmdStr.toString();
    }

    /**
     * 获得一级目录名称
     *
     * @param name
     * @return
     */
    private String getFirstPath(String name) {
        if (name == null)
            return null;
        return name.substring(0, 6);
    }

    /**
     * 获得二级目录名称
     *
     * @param name
     * @return
     */
    private String getSecondPath(String name) {
        if (name == null)
            return null;
        return name.substring(6, 7);
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
            log.debug("----GraphicsImage----convertType---------command:" + command);
            connection = gMService.getConnection();
            connection.execute(command);
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if (connection != null)
                connection.close();
        }
        log.debug("----GraphicsImage----convertType---------end 耗时" + (System.currentTimeMillis() - begin));
    }

    private String getConvertTpyrCommand(String src, String desc) {
        //去杂质
        return " convert" + " -strip -define jpeg:preserve-settings " + " -quality 100" + " " + src + " " + desc;
    }
}
