package com.ai.paas.ipaas.ips;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.base.interfaces.IDSSClient;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.utils.AuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * @author DOUXF
 *
 */
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 480288676615282980L;
	private static AuthDescriptor ad = null;
	private static IDSSClient dc = null;
	private static Gson gson = new Gson();

	@Override
	public void init() throws ServletException {
		// 获取dss客户端
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
		super.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String fileName = request.getParameter("fileName");
		if (!StringUtil.isBlank(fileName)) {
			File file = new File(request.getServletContext().getRealPath("/")
					+ "images/" + fileName);
			if (file.exists()) {
				int bytes = 0;
				ServletOutputStream op = response.getOutputStream();

				response.setContentType(getMimeType(file));
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "inline; filename=\""
						+ file.getName() + "\"");

				byte[] bbuf = new byte[1024];
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));

				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}

				in.close();
				op.flush();
				op.close();
			}
		} else if (request.getParameter("delFile") != null
				&& !request.getParameter("delFile").isEmpty()) {
			@SuppressWarnings("unused")
			String fileId = request.getParameter("delFile");
		} else if (request.getParameter("thumbName") != null
				&& !request.getParameter("thumbName").isEmpty()) {
			File file = new File(request.getServletContext().getRealPath("/")
					+ "images/" + request.getParameter("thumbName"));
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
				String mimetype = getMimeType(file);
				if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")
						|| mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
					BufferedImage im = ImageIO.read(file);
					if (im != null) {
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						if (mimetype.endsWith("png")) {
							ImageIO.write(im, "PNG", os);
							response.setContentType("image/png");
						} else if (mimetype.endsWith("jpeg")) {
							ImageIO.write(im, "jpg", os);
							response.setContentType("image/jpeg");
						} else if (mimetype.endsWith("jpg")) {
							ImageIO.write(im, "jpg", os);
							response.setContentType("image/jpeg");
						} else {
							ImageIO.write(im, "GIF", os);
							response.setContentType("image/gif");
						}
						ServletOutputStream srvos = response.getOutputStream();
						response.setContentLength(os.size());
						response.setHeader("Content-Disposition",
								"inline; filename=\"" + file.getName() + "\"");
						os.writeTo(srvos);
						srvos.flush();
						srvos.close();
					}
				}
			}
		} else {
			PrintWriter writer = response.getWriter();
			writer.write("call POST with multipart form data");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new IllegalArgumentException(
					"Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}

		ServletFileUpload uploadHandler = new ServletFileUpload(
				new DiskFileItemFactory());
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		JsonObject json = new JsonObject();
		try {
			List<FileItem> items = uploadHandler.parseRequest(request);
			for (FileItem item : items) {
				if (!item.isFormField()) {
					dc = DSSFactory.getClient(ad);
					String fileId = dc.save(item.get(), item.getName());
					json.addProperty("name", fileId);
					json.addProperty("size", item.getSize());
					json.addProperty("delete_url", "UploadServlet?delFile="
							+ fileId);
					json.addProperty("delete_type", "GET");

				}
			}
		} catch (FileUploadException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			writer.write(gson.toJson(json));
			writer.close();
		}

	}

	private String getMimeType(File file) {
		String mimetype = "";
		if (file.exists()) {
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
				mimetype = "image/png";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpg")) {
				mimetype = "image/jpg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpeg")) {
				mimetype = "image/jpeg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("gif")) {
				mimetype = "image/gif";
			} else {
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		return mimetype;
	}

	private String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		return suffix;
	}
}
