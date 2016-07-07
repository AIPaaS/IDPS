package com.ai.paas.ipaas.ips;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.base.interfaces.IDSSClient;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.utils.AuthUtil;
import com.ai.paas.ipaas.utils.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 图片服务器 上传图片时，处理图片格式（统一使用jpg格式），再保存到mongoDB
 *
 */
public class UploadImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914574498046477046L;
	private static final Logger log = Logger
			.getLogger(UploadImageServlet.class);

	private AuthDescriptor ad = null;
	private IDSSClient dc = null;
	private Gson gson = new Gson();
	private ImageUtil util;

	@Override
	public void init() throws ServletException {
		ad = AuthUtil.getAuthInfo();
		if (null == ad) {
			throw new ServletException(
					"Can not get auth info, pls. set in ENV or -DAUTH_URL=XXX -DAUTH_USER_PID -DAUTH_SRV_PWD -DAUTH_SRV_ID");
		}
		try {
			dc = DSSFactory.getClient(ad);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		util = new ImageUtil(ad);
		super.init();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		boolean success = false;
		log.debug("----保存本地图片----------------");
		String filename = null;
		String minWidth = null;
		String minHeight = null;
		FileOutputStream fos = null;
		InputStream in = null;
		JsonObject json = new JsonObject();
		try {
			in = request.getInputStream();
			filename = request.getHeader("filename");
			minWidth = request.getParameter("minWidth");
			minHeight = request.getParameter("minHeight");
			String path = util.getUplodPath();
			if (log.isInfoEnabled()) {
				log.info("upload request: path=" + path + ",file=" + filename);
			}
			File f1 = new File(path, filename);
			fos = new FileOutputStream(f1);
			byte[] buffer = new byte[1024];
			int bytes = 0;
			while ((bytes = in.read(buffer)) != -1) {
				fos.write(buffer, 0, bytes);
			}
			fos.flush();
			success = true;
		} catch (Exception e) {
			log.error("图片保存到本地出错：" + util.getUplodPath() + ",file:" + filename,
					e);
			success = false;
			json.addProperty("saveLocalFile:" + e.getMessage(), e
					.getStackTrace().toString());
		} finally {
			if (null != fos)
				fos.close();
			if (null != in)
				in.close();
		}
		if (success) {
			String name = getName();
			String id = "";

			try {

				// gm处理
				log.debug("----转化图片格式----------------");
				String ext = getFileExt(filename);
				log.debug("file=" + filename + " has extension:" + ext);
				// 此处要进行尺寸判断
				int _minWidth = 0;
				int _minHeight = 0;
				if (null != minWidth) {
					_minWidth = Integer.parseInt(minWidth);
				}
				if (null != minHeight) {
					_minHeight = Integer.parseInt(minHeight);
				}
				if (!util.judgeSize(filename, _minWidth, _minHeight)) {
					throw new IllegalArgumentException(
							"Image Size is illegal,minWidth:" + minWidth
									+ ",minHeight:" + minHeight);
				}
				util.convertType(filename, name + util.getSupportType(ext));
				log.debug("----保存到mongoDB----------------");
				id = dc.save(new File(getDestPath(name, ext)), filename);
				json.addProperty("id", id);
			} catch (Exception e) {
				success = false;
				log.error("图片格式转换、保存到mongodb出错：", e);
				// 这里反馈给客户端
				json.addProperty("saveLocalFile:" + e.getMessage(), e
						.getStackTrace().toString());
			}
		}
		json.addProperty("result", success ? "success" : "failure");
		response(response, gson.toJson(json));
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
			log.error("" + result, e);
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

	private String getDestPath(String name, String ext) {
		return util.getDestPath().endsWith(File.separator) ? (util
				.getDestPath() + name + util.getSupportType(ext)) : (util
				.getDestPath() + File.separator + name + util
				.getSupportType(ext));
	}

	private String getName() {
		return UUID.randomUUID() + "";
	}

	private String getFileExt(String name) {
		if (StringUtil.isBlank(name))
			return null;
		if (name.lastIndexOf(".") >= 0) {
			return name.substring(name.lastIndexOf("."));
		} else {
			return null;
		}
	}
}
