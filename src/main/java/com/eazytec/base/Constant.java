package com.eazytec.base;

public class Constant {
	
	/**
	 * Description:接口返回结果信息值
	 * 作者 : 蒋晨   
	 * 时间 : 2017-2-9  下午14:56:48
	 */
	public static enum RESULT_MSG{
		;
		//成功
		public static final String SUCCESS_MSG = "成功";
		//上传失败
		public static final String UPLOAD_FAILED_MSG = "上传失败";				
		//登录账号不存在
		public static final String LOGIN_ACCOUNT_NOT_EXISTS_MSG = "登录账号不存在";
		//密码错误
		public static final String PASSWORD_ERROR_MSG = "密码错误";
		//请求参数为空
		public static final String REQUEST_PARAM_EMPTY_MSG = "具体请求参数为空";
		//返回结果为空
		public static final String RETURN_NULL_EMPTY_MSG = "返回结果为空";
		//系统异常
		public static final String SYSTEM_ERROR_MSG= "系统异常";
		//无使用的权限
		public static final String NO_USE_MSG = "无使用权限";
		//保存失败
		public static final String SAVE_FAILED_MSG = "保存失败";	
	}
	
	/**
	 * Description:接口返回结果状态码
	 * 作者 : 蒋晨    
	 * 时间 : 2016-9-2 上午9:11:48
	 */
	public static enum RESULT_CODE{
		;
		//成功
		public static final String SUCCESS_CODE = "1";
		//登录账号不存在
		public static final String LOGIN_ACCOUNT_NOT_EXISTS_CODE = "-1";
		//密码错误
		public static final String PASSWORD_ERROR_CODE = "-2";
		//具体请求参数为空
		public static final String REQUEST_PARAM_EMPTY_CODE = "-3";
		//返回结果为空
		public static final String RETURN_NULL_EMPTY_CODE = "-4";
		//上传失败
		public static final String UPLOAD_FAILED_CODE = "-5";
		//系统异常
		public static final String SYSTEM_ERROR_CODE = "-6";
		//无登录App的权限
		public static final String NO_LOGIN_CODE = "-7";
		//保存失败
		public static final String SAVE_FAILED_CODE = "-8";
	}
    
    //编码格式
    public static final String CHARACTER_ENCODING = "UTF-8";
    
    //请求数据
    public static final String REQUEST_DATA = "requestData";
    
}

