package com.eazytec.external.interceptor;

import static com.eazytec.external.Constants.NOAUTHORIZE;

import static com.eazytec.external.Constants.NOLOGIN;
import static com.eazytec.external.Constants.TOKEN;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eazytec.external.util.ExternalUtils;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;


/**
 * 对外接口验证拦截器
 * 包括普通接口验证、需登录状态接口验证
 * 
 * 不需要登录状态验证的接口，必须使用普通验证方式
 * 目前只有登录接口使用普通验证，所以判断较简单 uri.indexOf("logon")
 * 如果普通验证的接口较多，需通过url规则进行区分。
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		Map<String,Object> map = new HashMap<String,Object>();
		String token = ExternalUtils.getToken(request);
		
		if (StringUtils.isBlank(token)) {
			ExternalUtils.returnErrorMsg(map, NOAUTHORIZE, response);
			return false;
		}
		
		String uri = request.getRequestURI();
		
		if (uri.indexOf("logon") > 0 ) {
			if (!TOKEN.equals(token)) {
				//普通验证token不正确
				ExternalUtils.returnErrorMsg(map, NOAUTHORIZE, response);
				return false;
			}
		}else{
			
			User user = userManager.getUserByToken(token);
			
			if (user == null) {
				//用户登录验证token不正确
				ExternalUtils.returnErrorMsg(map, NOLOGIN, response);
				return false;
			}else{
				//将user放置到request中供后面的controller使用
				ExternalUtils.setUser(request, user);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
	@Autowired
	private UserManager userManager;
}