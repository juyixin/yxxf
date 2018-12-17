package com.eazytec.petitionLetter.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.crm.model.Record;
import com.eazytec.petitionLetter.dao.PetitionLetterPersonDao;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.petitionLetter.service.PetitionLetterPersonService;
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.PageBean;

@Service("petitionLetterPersonService")
@WebService(serviceName = "PetitionLetterPersonService", endpointInterface = "com.eazytec.petitionLetter.service")
public class PetitionLetterPersonServiceImpl extends GenericManagerImpl<PetitionLetterPerson, String> implements PetitionLetterPersonService{
	
	@Autowired
	private PetitionLetterPersonDao petitionLetterPersonDao;
    
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	@Override
	public List<PetitionLetterPerson> getPerson() {
		return petitionLetterPersonDao.getPerson();
	}
    
	
	/**
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	@Override
	public List<PetitionLetterPerson> searchPerson(String name) {
		return petitionLetterPersonDao.searchPerson(name);
	}
	
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午8:00:01
     */
	@Override
	public void savePerson(PetitionLetterPerson person) {
		 petitionLetterPersonDao.savePerson(person);
	}
	
	/**
     * Description:校验身份证的唯一性
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 上午9:06:01
     */
	@Override
	public int getCount(String sfzCode) {
		return petitionLetterPersonDao.getCount(sfzCode);
	}
    
	/**
     * Description:查看详情
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 上午9:58:38
     */
	@Override
	public PetitionLetterPerson getDetail(String id) {
		return petitionLetterPersonDao.getDetail(id);
	}
	
	/**
     * Description:更新数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 下午8:00:01
     */
	@Override
	public void updatePerson(PetitionLetterPerson person) {
		 petitionLetterPersonDao.updatePerson(person);
	}
    
	
	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午9:53:13
     */
	@Override
	public String deletePersonInfo(List<String> isFalse) {
		String message = "";
		try{
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for(int i=0;i<isFalse.size();i++){
				System.out.println("**********删除记录："+i+"**********" + currentTime.toGMTString());
				petitionLetterPersonDao.deletePersonInfo(isFalse.get(i));
			}
			message="true";
		}catch (Exception e){
			e.printStackTrace();
			log.warn(e.getMessage());
			message="false";
		}
		return message;
	}

    
	/**
     * Description:分页
     * 作者 : 蒋晨 
     * 时间 : 2017-2-27 上午8:29:13
     */
	@Override
	public PageBean<PetitionLetterPerson> getRecords(PageBean<PetitionLetterPerson> pageBean,String name,String sfzCode){
		int totalrecords =  petitionLetterPersonDao.getAllRecordCount(pageBean, name,sfzCode);
		pageBean.setTotalrecords(totalrecords);
		List<PetitionLetterPerson> result =  petitionLetterPersonDao.getAllRecord(pageBean, name, sfzCode);
		pageBean.setResult(result);
		return pageBean;
	}

}
