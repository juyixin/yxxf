package com.eazytec.link.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.base.Constant;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.link.dao.LinkDao;
import com.eazytec.link.model.Link;
import com.eazytec.link.model.LoginVo;
import com.eazytec.link.returns.LoginReturn;
import com.eazytec.link.service.LinkService;
import com.eazytec.model.User;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("linkService")
@WebService(serviceName = "LinkService", endpointInterface = "com.eazytec.link.service")
public class LinkServiceImpl extends GenericManagerImpl<Link, String> implements LinkService{
	
	private  LinkDao  linkDao;
       
    @Autowired
   	public LinkServiceImpl(LinkDao linkDao) {
   		super(linkDao);
   		this.linkDao = linkDao;
   	}
   	
   	@Autowired
   	public void setLinkDao(LinkDao linkDao) {
   		this.linkDao = linkDao;
   	}
    
   	
   	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@Override
	public List<Link> getLink() {
		return linkDao.getLink();
	}
	
	
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-7  上午9:36:35
	 */
	@Override
	public List<Link> searchLink(String name) {
		return linkDao.searchLink(name);
	}
    
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	@Override
	public Link saveLinkInfo(Link link) {
		return linkDao.saveLinkInfo(link);
	}
    
	
	/**
	 * Description:切换状态
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-6 下午1:45:23
	 */
	@Override
	public void updateStatus(String id, String status) {
		linkDao.updateStatus(id,status);
	}
    
	
	/**
	 * Description:获取详情
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-6 下午6:28:54
	 */
	@Override
	public Link getDetail(String id) {
		return linkDao.get(id);
	}
	
	/**
 	 * Description:修改数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-6上午9:16:09
 	 */
	@Override
	public Link updateLinkInfo(Link link) {
		return linkDao.updateLinkInfo(link);
	}
	
	/**
 	 * Description:删除数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-7 上午10:10:10
 	 */
	@Override
	public String deleteLinkInfo(List<String> isFalse) {
		String message = "";
		try{
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for(int i=0;i<isFalse.size();i++){
				System.out.println("**********删除友情链接记录："+i+"**********" + currentTime.toGMTString());
				linkDao.deleteLinkInfo(isFalse.get(i));
			}
			message="true";
		}catch (Exception e){
			e.printStackTrace();
			log.warn(e.getMessage());
			message="false";
		}
		return message;
	}

	@Override
	public List<Link> getLink1(String isActive) {
		return linkDao.getLink1(isActive);
	}

	
/*	public String loginInfo(String loginAccount,String loginPassword) {
		LoginReturn loginReturn = new LoginReturn();
		LoginVo loginVo = new LoginVo();
		if(loginAccount.isEmpty()||loginPassword.isEmpty()){
			User user = linkDao.getUser(loginAccount);
			if(user!=null){
				String isPassword = loginAccount + loginPassword;
				MessageDigest md;
				StringBuffer encrypted = new StringBuffer();
				try {
					md = MessageDigest.getInstance("MD5");
					md.update(isPassword.getBytes());
				    byte byteData[] = md.digest();
				    for (int i = 0; i < byteData.length; i++)
				    	encrypted.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				} catch (NoSuchAlgorithmException e) {
					throw new EazyBpmException(e.getMessage(),e);
				}
				isPassword =  encrypted.toString();
				String passWord = user.getPassword();
				if(isPassword.equals(passWord)){
					loginVo.setLoginAccount(user.getId());
					loginVo.setLoginName(user.getFirstName());
					loginVo.setPhone(user.getMobile());
					loginVo.setLoginPassword(loginPassword);
					loginReturn.setLoginvo(loginVo);
					loginReturn.setCode(Constant.RESULT_CODE.SUCCESS_CODE);
					loginReturn.setMessage(Constant.RESULT_MSG.SUCCESS_MSG);
				}else{
					loginReturn.setCode(Constant.RESULT_CODE.PASSWORD_ERROR_CODE);
					loginReturn.setMessage(Constant.RESULT_MSG.PASSWORD_ERROR_MSG);
				}
			}else{
				loginReturn.setCode(Constant.RESULT_CODE.LOGIN_ACCOUNT_NOT_EXISTS_CODE);
				loginReturn.setMessage(Constant.RESULT_MSG.LOGIN_ACCOUNT_NOT_EXISTS_MSG);
			}
		}else{
			loginReturn.setCode(Constant.RESULT_CODE.REQUEST_PARAM_EMPTY_CODE);
			loginReturn.setMessage(Constant.RESULT_MSG.REQUEST_PARAM_EMPTY_MSG);
		}
		JSONObject obj = JSONObject.fromObject(loginReturn);
		return obj.toString();
	}*/

}
