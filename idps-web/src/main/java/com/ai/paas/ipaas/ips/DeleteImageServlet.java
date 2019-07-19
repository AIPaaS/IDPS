package com.ai.paas.ipaas.ips;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.paas.ipaas.dss.DSSBaseFactory;
import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.IDSSClient;
import com.ai.paas.ipaas.image.ImageAuthDescriptor;
import com.ai.paas.ipaas.utils.AuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DeleteImageServlet extends HttpServlet {
    private static Logger log = LoggerFactory.getLogger(DeleteImageServlet.class);
	private static final long serialVersionUID = 1594325791647123L;
	
	private ImageAuthDescriptor ad = null;
	private IDSSClient dc = null;

	@Override
	public void init() throws ServletException {
		ad = AuthUtil.getAuthInfo();
		if (null == ad) {
			throw new ServletException(
					"Can not get auth info, pls. set in ENV or -DAUTH_URL=XXX -DAUTH_USER_PID -DAUTH_SRV_PWD -DAUTH_SRV_ID");
		}
		try {
			if (ad.isCompMode()) {
				dc = DSSBaseFactory.getClient(ad.getMongoInfo());
			} else {
				dc = DSSFactory.getClient(ad);
			}	
		} catch (Exception e) {
			throw new ServletException(e);
		}
		super.init();
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		long begin = System.currentTimeMillis();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String imageId = request.getParameter("imageId");
		dc.delete(imageId);
		
		String uri = request.getRequestURI();
		log.debug(uri + "-----:共耗时"+ (System.currentTimeMillis() - begin) + "ms");
		
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		response(response, new Gson().toJson(json));
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
			log.error("", e);
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
	
}
