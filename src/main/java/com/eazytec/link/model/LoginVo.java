package com.eazytec.link.model;


/**
 * Description:登录信息实体类(APP)
 * 作者 : 蒋晨 
 * 时间 : 2017-2-14 上午10:38:35
 */
public class LoginVo {
	
	//登录账号
	private String loginAccount;
	//登录密码
	private String loginPassword;
    //手机号码
	private String phone;
	//姓名
	private String loginName;
	public String getLoginAccount() {
		return loginAccount;
	}
	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
