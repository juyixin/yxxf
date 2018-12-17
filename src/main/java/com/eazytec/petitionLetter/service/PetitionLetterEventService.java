package com.eazytec.petitionLetter.service;

import java.util.List;

import javax.jws.WebService;

import com.eazytec.petitionLetter.model.PetitionLetterEvent;

@WebService
public interface PetitionLetterEventService {
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午10:51:51
     */
	public List<PetitionLetterEvent> getEvent(String personId);
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午10:51:51
     */
	public List<PetitionLetterEvent> getEvent1(String personId);
	
	
	/**
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26  下午8:35:51
     */
	public List<PetitionLetterEvent> searchEvent(String name);
	
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午2:22:22
     */
	public void saveEvent(PetitionLetterEvent event);
	
	
	/**
     * Description:查看数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午2:22:22
     */
	public PetitionLetterEvent getDetail(String id);
	
	/**
     * Description:更新数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26 下午7:29:29
     */
	public void updateEvent(PetitionLetterEvent event);
	
	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26 下午7:29:29
     */
	public String deleteEvent(List<String> isFalse);

}
