package com.ai.paas.ipaas.ips;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
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
import com.ai.paas.ipaas.dss.interfaces.IDSSClient;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfo;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.ips.impl.GMImage;
import com.ai.paas.ipaas.ips.service.IImageService;
import com.ai.paas.ipaas.ips.service.impl.ImageSvImpl;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.utils.GsonUtil;
import com.ai.paas.ipaas.utils.ImageUtil;
import com.ai.paas.ipaas.utils.PaasException;
import com.google.gson.Gson;



@Component
public class DeletefImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1594325791647123L;

	private static final Logger log = Logger.getLogger(DeletefImageServlet.class);
	private  AuthDescriptor ad = null;
	private  IDSSClient dc = null;
	
	private static final String AUTH = "AUTH";
	private static final String AUTH_URL = "AUTH_URL";
	
	private String dssServiceId;
	private String dssServicePwd;
	private String userPid;
	private IImageService iImageService ;
	private IpaasSysConfig config ;
	
	

	
	@Override
	public void init() throws ServletException {
		iImageService = new ImageSvImpl();
		 List<IpaasSysConfig> configlist = iImageService.searchConfig(AUTH, AUTH_URL);
		 config = configlist.get(0);
		super.init();
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException  {
		
		long begin = System.currentTimeMillis();
		HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        String userId = request.getParameter("userId");
        String ipsServiceId = request.getParameter("serviceId");
        String imageId = request.getParameter("imageId");
        if(dssServiceId==null){
        	List<IdpsInstanceBandDss> dsslist = iImageService.srarchDssInfo(userId, ipsServiceId);
        	if(dsslist.size()==0 ){
					throw new ServletException("No config DSS info!");
        	}
        	IdpsInstanceBandDss dss = dsslist.get(0);
        	userPid =dss.getDssPid();
    		dssServicePwd = dss.getDssServicePwd();
    		dssServiceId = dss.getDssServiceId();
        }
        boolean success = true; 
        Map map = new HashMap(); 
        ad =  new AuthDescriptor(config.getFieldValue(), userPid, dssServicePwd,dssServiceId);
		try {
			dc = DSSFactory.getClient(ad);
			dc.delete(imageId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			success = false;
			e.printStackTrace();
		}
		map.put("result", success?"success":"failure");
		response(response,GsonUtil.objToGson(map));
//        log.debug(uri+"-------------------:共耗时"+(System.currentTimeMillis()-begin)+"ms");
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
}
