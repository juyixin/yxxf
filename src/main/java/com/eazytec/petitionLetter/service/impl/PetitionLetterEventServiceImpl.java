package com.eazytec.petitionLetter.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.petitionLetter.dao.PetitionLetterEventDao;
import com.eazytec.petitionLetter.model.PetitionLetterEvent;
import com.eazytec.petitionLetter.service.PetitionLetterEventService;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("petitionLetterEventService")
@WebService(serviceName = "PetitionLetterEventService", endpointInterface = "com.eazytec.petitionLetter.service")
public class PetitionLetterEventServiceImpl extends GenericManagerImpl<PetitionLetterEvent, String> implements PetitionLetterEventService{
        
	@Autowired
	private PetitionLetterEventDao petitionLetterEventDao;
    
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午10:51:51
     */
	@Override
	public List<PetitionLetterEvent> getEvent(String personId) {
		return petitionLetterEventDao.getEvent(personId);
	}
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午10:51:51
     */
	@Override
	public List<PetitionLetterEvent> getEvent1(String personId) {
		return petitionLetterEventDao.getEvent1(personId);
	}

    
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午2:22:22
     */
	@Override
	public void saveEvent(PetitionLetterEvent event) {
		petitionLetterEventDao.saveEvent(event);
	}
	
	
	/**
     * Description:更新数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26 下午7:29:29
     */
	@Override
	public void updateEvent(PetitionLetterEvent event) {
		petitionLetterEventDao.updateEvent(event);
	}

	/**
     * Description:查看数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午2:22:22
     */
	@Override
	public PetitionLetterEvent getDetail(String id) {
		return petitionLetterEventDao.getDetail(id);		
	}

	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26 下午7:29:29
     */
	@Override
	public String deleteEvent(List<String> isFalse) {
		String message = "";
		try{
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for(int i=0;i<isFalse.size();i++){
				System.out.println("**********删除记录："+i+"**********" + currentTime.toGMTString());
				petitionLetterEventDao.deleteEvent(isFalse.get(i));
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
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26  下午8:35:51
     */
	@Override
	public List<PetitionLetterEvent> searchEvent(String name) {
		return petitionLetterEventDao.searchEvent(name);
	}
}
