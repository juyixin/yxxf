package com.eazytec.link.returns;

import com.eazytec.link.model.LoginVo;

/**
 * Description:登录接口返回实体类(APP)
 * 作者 : 蒋晨 
 * 时间 : 2017-2-14 上午10:38:35
 */
public class LoginReturn {
	//登录信息类
	private LoginVo loginvo;
	//返回code码
	private String code;
	//返回message类
	private String message;
	
	
	public LoginVo getLoginvo() {
		return loginvo;
	}
	public void setLoginvo(LoginVo loginvo) {
		this.loginvo = loginvo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
