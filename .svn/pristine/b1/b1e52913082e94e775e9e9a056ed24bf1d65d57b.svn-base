package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.external.util.ExternalUtils;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;

@Controller("extLoginController")
public class LogonController {
	private static final Logger log = LoggerFactory.getLogger(LogonController.class);

	@Autowired
	private UserManager userManager;
	@Autowired
	private PasswordEncoder pwdEncoder;

	@RequestMapping(value = "logon", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(String username, String password, String clientVersion, String clientType,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {

			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return ExternalUtils.error(map, "请输入账号和密码");
			}

			User user = userManager.getUserByUsername(username);
			if (user == null) {
				return ExternalUtils.error(map, "登录失败，账户或密码不正确");
			}

			if (!pwdEncoder.encodePassword(password, username).equals(user.getPassword())) {
				return ExternalUtils.error(map, "登录失败，账户或密码不正确");
			}

			if (!user.isEnabled()) {
				return ExternalUtils.error(map, "该账户正在审核中或已被禁用");
			}
			
			//创建用户验证token，并更新到数据库
			String token = ExternalUtils.createToken(username, password, clientVersion, clientType);
			
			user.setToken(token);
			userManager.updateUser(user);
			
			map.put("id", user.getId());
			map.put("password", password);
			map.put("username", user.getUsername());
			map.put("mobile", user.getMobile());
			map.put("email", user.getEmail());
			map.put("fullName", user.getFullName());
			
			map.put("token", token);//返回用户token供移动端使用
			
			return ExternalUtils.success(map);
		} catch (Exception e) {
			log.error("登录失败，请稍后再尝试登录。", e);
			return ExternalUtils.error(map, "登录失败，请稍后再尝试登录。");
		}
		
	}
}
