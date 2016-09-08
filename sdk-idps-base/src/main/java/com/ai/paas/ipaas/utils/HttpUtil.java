package com.ai.paas.ipaas.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {
	private static final Log logger = LogFactory.getLog(HttpUtil.class);
	
	private static int connectionTimeout = 60000;
	private static int readTimeout = 60000;

	/**
	 * 图片服务器 上传图片
	 * @param url
	 * @return
	 */
	public static String upImage(String url, byte[] image, String name, int minWidth, int minHeight, String token, String needAuth) {
		HttpURLConnection connection = null;
		BufferedReader in = null;
		String result = "";

		try {
			URL logoutUrl = new URL(url);
			connection = (HttpURLConnection) logoutUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setReadTimeout(readTimeout);
			connection.setConnectTimeout(connectionTimeout);
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=gc0p4Jq0M2Yt08jU534c0p;file=" + name);
			connection.setRequestProperty("filename", name);
			connection.setRequestProperty("minWidth", "" + minWidth);
			connection.setRequestProperty("minHeight", "" + minHeight);
			connection.setRequestProperty("token", token);
			connection.setRequestProperty("needAuth", needAuth);

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());

			out.write(image);
			out.flush();
			out.close();
			if (200 == connection.getResponseCode()) {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String s = null;
				while ((s = in.readLine()) != null) {
					result += s;
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Finished sending message to" + url);
				}
			}
			return result;
		} catch (SocketTimeoutException e) {
			logger.warn("Socket Timeout Detected while attempting to send message to ["
					+ url + "].");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			if (connection != null)
				connection.disconnect();
		}
		
		return null;
	}

	/**
	 * 图片服务器 删除图片
	 * @param url
	 * @return
	 */
	public static boolean delImage(String url, String token, String needAuth) {
		HttpURLConnection connection = null;
		boolean result = false;

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Attempting to access " + url);
			}
			URL logoutUrl = new URL(url);

			connection = (HttpURLConnection) logoutUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setReadTimeout(readTimeout);
			connection.setConnectTimeout(connectionTimeout);
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("token", token);
			connection.setRequestProperty("needAuth", needAuth);
			connection.connect();
			connection.getInputStream();
			
			if (200 == connection.getResponseCode()) {
				result = true;
			}
		} catch (SocketTimeoutException e) {
			logger.warn("Socket Timeout Detected while attempting to send message to ["
					+ url + "].");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		
		return result;
	}
	
	/**
	 * 获取图片
	 * @param url
	 * @return byte[]
	 */
	public static byte[] getImage(String url) {
		HttpURLConnection connection = null;
		BufferedReader in  = null;
		byte[] result = null;

		try {
			URL logoutUrl = new URL(url);
			connection = (HttpURLConnection) logoutUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setReadTimeout(readTimeout);
			connection.setConnectTimeout(connectionTimeout);
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
			connection.connect();
			
		    String line = null ;
		    StringBuilder sb = new StringBuilder ();
		    in  = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null ) {
                 sb.append(line + "/n" );
            }
            result = sb.toString().getBytes();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return result;
	}
}