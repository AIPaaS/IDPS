package com.ai.paas.ipaas.ips;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Component;

import com.ai.paas.ipaas.dss.DSSFactory;
import com.ai.paas.ipaas.dss.interfaces.IDSSClient;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.ips.service.IImageService;
import com.ai.paas.ipaas.ips.service.impl.ImageSvImpl;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.utils.ImageUtil;
import com.google.gson.Gson;

@Component
public class UploadServlet extends HttpServlet {
	
	
	private static final String AUTH_ADDR = "http://10.1.228.200:14105/iPaas-Auth/service/auth";
	private static AuthDescriptor ad = null;
	private static IDSSClient dc = null;
	private static Gson gson = new Gson();
	
	
	private static final String AUTH = "AUTH";
	private static final String AUTH_URL = "AUTH_URL";
    
	private IImageService iImageService = new ImageSvImpl();
	
    /**
        * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
        * 
        */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        if (request.getParameter("getfile") != null && !request.getParameter("getfile").isEmpty()) {
            File file = new File(request.getServletContext().getRealPath("/")+"imgs/"+request.getParameter("getfile"));
            if (file.exists()) {
                int bytes = 0;
                ServletOutputStream op = response.getOutputStream();

                response.setContentType(getMimeType(file));
                response.setContentLength((int) file.length());
                response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );

                byte[] bbuf = new byte[1024];
                DataInputStream in = new DataInputStream(new FileInputStream(file));

                while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, bytes);
                }

                in.close();
                op.flush();
                op.close();
            }
        } else if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
        	String fileId = request.getParameter("delfile");
        } else if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
            File file = new File(request.getServletContext().getRealPath("/")+"imgs/"+request.getParameter("getthumb"));
                if (file.exists()) {
                    System.out.println(file.getAbsolutePath());
                    String mimetype = getMimeType(file);
                    if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")|| mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
                        BufferedImage im = ImageIO.read(file);
//                        if (im != null) {
//                            BufferedImage thumb = Scalr.resize(im, 75); 
//                            ByteArrayOutputStream os = new ByteArrayOutputStream();
//                            if (mimetype.endsWith("png")) {
//                                ImageIO.write(thumb, "PNG" , os);
//                                response.setContentType("image/png");
//                            } else if (mimetype.endsWith("jpeg")) {
//                                ImageIO.write(thumb, "jpg" , os);
//                                response.setContentType("image/jpeg");
//                            } else if (mimetype.endsWith("jpg")) {
//                                ImageIO.write(thumb, "jpg" , os);
//                                response.setContentType("image/jpeg");
//                            } else {
//                                ImageIO.write(thumb, "GIF" , os);
//                                response.setContentType("image/gif");
//                            }
//                            ServletOutputStream srvos = response.getOutputStream();
//                            response.setContentLength(os.size());
//                            response.setHeader( "Content-Disposition", "inline; filename=\"" + file.getName() + "\"" );
//                            os.writeTo(srvos);
//                            srvos.flush();
//                            srvos.close();
//                        }
                    }
            } // TODO: check and report success
        } else {
            PrintWriter writer = response.getWriter();
            writer.write("call POST with multipart form data");
        }
    }
    
    /**
        * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
        * 
        */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }

        ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        List json = new ArrayList();
        try {
            List<FileItem> items = uploadHandler.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                	

                		Map jsono = new HashMap();
//                		String fileId = MongoFileUtil.saveFile(item.get(),item.getName(), getSuffix(item.getName()));
                		
                		String userId    = "FFF49D0D518948D0AB28D7A8EEE25D03";
                		String ipsServiceId = "IDPS001";
                		List<IdpsInstanceBandDss> dsslist = iImageService.srarchDssInfo(userId, ipsServiceId);
                        List<IpaasSysConfig> configlist = iImageService.searchConfig(AUTH, AUTH_URL);
                        IpaasSysConfig config = configlist.get(0);
                        String authAdd = config.getFieldValue();
                        if(dsslist.size()==0){
                			return;
                        }
                        IdpsInstanceBandDss dss = dsslist.get(0);
                		String userPid =dss.getDssPid();
                		String dssServicePwd = dss.getDssServicePwd();
                		String dssServiceId = dss.getDssServiceId();
                		ad =  new AuthDescriptor(AUTH_ADDR, userPid, dssServicePwd,dssServiceId);
        				dc = DSSFactory.getClient(ad);
        				String fileId = dc.save(item.get(),item.getName());
                        jsono.put("name",fileId);
                        jsono.put("size", item.getSize());
                        jsono.put("url", ImageUtil.getStaticDocUrl(fileId, "doc"));
//                        jsono.put("thumbnail_url", "UploadServlet?getthumb=" + item.getName());
                        jsono.put("delete_url", "UploadServlet?delfile=" + fileId);
                        jsono.put("delete_type", "GET");
                        json.add(jsono);
                        System.out.println(json.toString());
                        System.out.println(json.toString());
                        
                }
            }
        } catch (FileUploadException e) {
                throw new RuntimeException(e);
        } catch (Exception e) {
                throw new RuntimeException(e);
        } finally {
            writer.write(json.toString());
            writer.close();
        }

    }


	private String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpg")){
                mimetype = "image/jpg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpeg")){
                mimetype = "image/jpeg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("gif")){
                mimetype = "image/gif";
            }else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype  = mtMap.getContentType(file);
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
