package com.ai.paas.ipaas.ips;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.impl.DSSClient;
import com.ai.paas.ipaas.dss.interfaces.IDSSClient;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfo;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.ips.service.IImageService;
import com.ai.paas.ipaas.ips.service.impl.ImageSvImpl;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.utils.GsonUtil;
import com.ai.paas.ipaas.utils.ImageUtil;
import com.google.gson.Gson;


/**
 * 图片服务器
 * 上传图片时，处理图片格式（统一使用jpg格式），再保存到mongoDB
 *
 */
@Component
public class UpImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914574498046477046L;
	private static final Logger log = Logger.getLogger(UpImageServlet.class);

	private static final String upImagePath = "/aifs01/devusers/devusr01/";

//	private static final String AUTH_ADDR = "http://10.1.228.200:14105/iPaas-Auth/service/auth";
	
	private static final String AUTH = "AUTH";
	private static final String AUTH_URL = "AUTH_URL";
	private AuthDescriptor ad = null;
	private IDSSClient dc = null;
	private Gson gson = new Gson();

	private String dssServiceId;
	private String dssServicePwd;
	private String userPid;
	private IImageService iImageService ;
	private IdpsConfigInfo configInfo;
	private IpaasSysConfig config ;
	
	
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		iImageService = new ImageSvImpl();
		
		 List<IpaasSysConfig> configlist = iImageService.searchConfig(AUTH, AUTH_URL);
		 config = configlist.get(0);
		 List<IdpsConfigInfo> list =  iImageService.search();
		configInfo = list.get(0);
		super.init();
	}
	
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		
		String userId = request.getParameter("userId");
		String ipsServiceId = request.getParameter("serviceId");
		
		if(dssServiceId==null){
        	List<IdpsInstanceBandDss> dsslist = iImageService.srarchDssInfo(userId, ipsServiceId);
        	IdpsInstanceBandDss dss = dsslist.get(0);
        	userPid =dss.getDssPid();
    		dssServicePwd = dss.getDssServicePwd();
    		dssServiceId = dss.getDssServiceId();
        }
        String authAdd = config.getFieldValue();
		boolean success = true; 
		List<IdpsConfigInfo> list =  iImageService.search();
		IdpsConfigInfo configInfo = list.get(0);
		configInfo.setAuthUrl(config.getFieldValue());
		ImageUtil util = new ImageUtil(gson.toJson(configInfo),userPid,dssServiceId,dssServicePwd);
		Gson json = new Gson();
		Map map = new HashMap(); 
		log.debug("----保存本地图片----------------");
		String filename = null;
		FileOutputStream fos = null;
		InputStream in = null; 
		try {
			in = request.getInputStream();
			filename = request.getHeader("filename");
			
			File f1 = new File(configInfo.getUpPath(), filename);
			fos = new FileOutputStream(f1);
			byte[] buffer = new byte[1024];
			int bytes = 0;
			while ((bytes = in.read(buffer)) != -1) {
				fos.write(buffer, 0, bytes);
			}
			fos.flush();
		} catch (Exception e) {
			log.error("图片保存到本地出错：", e);
			success = false;
		} finally {
			fos.close();
			in.close();
		}
		if(success){
			String name = getName();
			String id = "";
			
			/**
			String type = getType(filename);
			if(ImageUtil.getSupportType().equals(type)){
				log.debug("----直接保存到mongoDB----------------");
				try {
					id = ImageUtil.saveToRomte(new File(ImageUtil.getUpPath(),filename), filename, ImageUtil.getSupportType());
				} catch (Exception e) {
					log.error("直接保存到mongoDB出错：", e);
					success = false;
				}
				json.put("id", id);
			}else{
		    	try {
		    		//gm处理
					log.debug("----转化图片格式----------------");
					ImageUtil.convertType(filename,name+ImageUtil.getSupportType());
					log.debug("----保存到mongoDB----------------");
					id = ImageUtil.saveToRomte(new File(getNewPathName(name)), filename, ImageUtil.getSupportType());
					json.put("id", id);
				} catch (Exception e) {
					success = false;
					log.error("图片格式转换、保存到mongodb出错：", e);
				}
			}
			*/
			//2014-12-08 都做图片去杂质处理
			try {
	    		//gm处理
				log.debug("----转化图片格式----------------");
				util.convertType(filename,name+util.getSupportType());
				log.debug("----保存到mongoDB----------------");
				ad =  new AuthDescriptor(config.getFieldValue(), userPid, dssServicePwd,dssServiceId);
				dc = DSSFactory.getClient(ad);
				id = dc.save(new File(getNewPathName(name)), filename);
				map.put("id", id);
			} catch (Exception e) {
				success = false;
				log.error("图片格式转换、保存到mongodb出错：", e);
			}
		}
		map.put("result", success?"success":"failure");
		response(response,GsonUtil.objToGson(map));
	}
	
	
	private String getType(String name) {
		if(name!=null)
			return name.substring(name.indexOf("."));
		return null;
	}


	private void response(HttpServletResponse response, String result) {
			ServletOutputStream outStream = null;
     		try {
     			response.setHeader("Pragma", "No-cache");  
     	        response.setHeader("Cache-Control", "no-cache");  
     	        response.setDateHeader("Expires", 0);  
     			response.setContentType("text/html;charset=UTF-8");
     			outStream = response.getOutputStream();
     			outStream.print(result);
    			outStream.flush();
    			log.debug("--return------------------ok");
     		} catch (Exception e) {
				log.error("",e);
     		}finally{
     			try {
					if(outStream!=null){
						outStream.close();
					}
				} catch (Exception e) {
					log.error("",e);			
				}
		
     		}
	}


	private String getNewPathName(String name) {
		return ImageUtil.getUpPathNew().endsWith(File.separator)?(ImageUtil.getUpPathNew()+name+ImageUtil.getSupportType()):
			(ImageUtil.getUpPathNew()+File.separator+name+ImageUtil.getSupportType());
	}


	private String getName() {
		return UUID.randomUUID()+"";
	}
	

}
