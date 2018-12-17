package com.eazytec.petitionLetter.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.util.PageBean;

public interface PetitionLetterPersonDao extends GenericDao<PetitionLetterPerson,String>{
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	public List<PetitionLetterPerson> getPerson();
	
	
	/**
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	public List<PetitionLetterPerson> searchPerson(String name);
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午8:00:01
     */
	public void savePerson(PetitionLetterPerson person);
	
    
	/**
     * Description:校验身份证的唯一性
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 上午9:06:01
     */
	public int getCount(String sfzCode);
	
	
	/**
     * Description:查看详情
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 上午9:58:38
     */
	public PetitionLetterPerson getDetail(String id);
	
	/**
     * Description:更新数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 下午8:00:01
     */
	public void updatePerson(PetitionLetterPerson person);
	
	
	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午9:53:13
     */
	public void deletePersonInfo(String id);
	
	int getAllRecordCount(PageBean<PetitionLetterPerson> pageBean,String num,String sfzCode);
	
	List<PetitionLetterPerson> getAllRecord(PageBean<PetitionLetterPerson> pageBean,String name, String sfzCode);
}
