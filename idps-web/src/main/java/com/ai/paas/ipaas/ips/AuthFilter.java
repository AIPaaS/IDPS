package com.ai.paas.ipaas.ips;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.paas.ipaas.image.ImageAuthDescriptor;
import com.ai.paas.ipaas.uac.service.UserClientFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.ai.paas.ipaas.uac.vo.AuthResult;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.utils.AuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet Filter implementation class AuthFilter
 */
public class AuthFilter implements Filter {
	private Gson gson = new Gson();
	private ImageAuthDescriptor imageAD = null;

	/**
	 * Default constructor.
	 */
	public AuthFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// place your code here
		// 转换成http resquest
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String token = req.getHeader("token");

		if (imageAD.isCompMode()) {
			chain.doFilter(req, resp);
		} else {
			if (StringUtil.isBlank(token)) {
				// 返回认证失败
				checkFail(resp);
				return;
			}
			// 开始解密
			String params = CiperUtil.decrypt(AuthConstant.IDPS_SEC_KEY, token);
			if (StringUtil.isBlank(params)) {
				checkFail(resp);
				return;
			}
			// 每次都认证一下？上传和删除还可以
			AuthDescriptor ad = new AuthDescriptor();
			JsonObject json = gson.fromJson(params, JsonObject.class);
			ad.setAuthAdress(imageAD.getAuthAdress());
			ad.setPid(json.get("pid").getAsString());
			ad.setPassword(json.get("srvPwd").getAsString());
			ad.setServiceId(json.get("srvId").getAsString());
			AuthResult authResult = UserClientFactory.getUserClient().auth(ad);
			if (null == authResult || null == authResult.getUserId()) {
				checkFail(resp);
				return;
			}
			ad = null;
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}
	}

	private void checkFail(HttpServletResponse resp) throws IOException,
			ServletException {
		resp.setStatus(401);
		resp.setHeader("Cache-Control", "no-store");
		resp.setDateHeader("Expires", 0);
		resp.setHeader("WWW-authenticate", "Basic Realm=\"idps\"");
		resp.flushBuffer();
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		imageAD = AuthUtil.getAuthInfo();
		if (null == imageAD) {
			throw new ServletException(
					"Can not get auth info, pls. set in ENV or -DAUTH_URL=XXX -DAUTH_USER_PID -DAUTH_SRV_PWD -DAUTH_SRV_ID -DisCompMode -DmongoInfo");
		}

	}

}
