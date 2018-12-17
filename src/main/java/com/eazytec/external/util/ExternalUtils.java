package com.eazytec.external.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import com.eazytec.model.User;


public class ExternalUtils {
	
	public static final String USER_KEY = "_user_key";
	
	/**
	 * 禁止实例化
	 */
	private ExternalUtils() {}
	
	
	/**
	 * 创建普通验证token
	 * @return
	 */
	public static String createToken() {
		
		MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
		
		//MD5加密后取前15个字节，默认使用EAZYTEC，需根据具体项目修改
		String token = encoder.encodePassword("EAZYTEC", null).substring(0,8);
		return token;
	}
	
	/**
	 * 创建登录验证token
	 * 该方法中没有使用设备唯一ID做为token的生成条件
	 * 如果有唯一设备登录的要求，需加上
	 * @param username
	 * @param password
	 * @param clientVersion
	 * @param clientType
	 * @return
	 */
	public static String createToken(String username, String password, String clientVersion,String clientType) {
		
		MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
		
		//MD5加密后取前8个字节
		String token = encoder.encodePassword(username + password + clientVersion + clientType, null).substring(0, 8);
		return token;
	}
	
	public static void setUser(HttpServletRequest request, User user) {
		request.setAttribute(USER_KEY, user);
	}
	
	public static User getUser(HttpServletRequest request) {
		return (User) request.getAttribute(USER_KEY);
	}

	public static String getUsername(HttpServletRequest request) {
		User user = getUser(request);
		if (user != null) {
			return user.getUsername();
		} else {
			return null;
		}
	}
	
	/**
	 * 从request请求头中获取token
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request){
		String token = request.getHeader("token");
		if (StringUtils.isBlank(token)) {
			return null;
		}
		return token.toLowerCase();
    }
	
	public static String getFileName(HttpServletRequest request){
		String fileName = request.getHeader("fileName");
		try {
			fileName = new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return fileName;
    }
	
	public static Map<String, Object> success(Map<String, Object> map) {
		map.put("success", true);
		return map;
	}
	
	public static Map<String, Object> error(Map<String, Object> map, String errorMsg) {
		map.put("success", false);
		map.put("errorMsg", errorMsg);
		return map;
	}

	public static void returnErrorMsg(Map<String, Object> map, String errorMsg, HttpServletResponse response) {

		ObjectMapper mapper = new ObjectMapper();

		map.put("success", false);
		map.put("errorMsg", errorMsg);
		try {
			ResponseUtils.renderJson(response, mapper.writeValueAsString(map));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
