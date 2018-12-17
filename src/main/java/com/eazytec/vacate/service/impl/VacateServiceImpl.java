package com.eazytec.vacate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
import com.eazytec.vacate.dao.VacateDao;
import com.eazytec.vacate.model.Vacate;
import com.eazytec.vacate.service.VacateService;

@Service
public class VacateServiceImpl implements VacateService{
	
	@Autowired
	private VacateDao dao;
	

	@Override
	public List<Vacate> getVacate(String startTime,String endTime) {
		// TODO Auto-generated method stub
		return dao.getVacate(startTime,endTime);
	}

	@Override
	public List<Vacate> searchVacate(String type,String state) {
		// TODO Auto-generated method stub
		return dao.searchVacate(type,state);
	}

	@Override
	public Vacate saveVacateInfo(Vacate Vacate) {
		// TODO Auto-generated method stub
		return dao.saveVacateInfo(Vacate);
	}

	@Override
	public Vacate getDetail(String id) {
		// TODO Auto-generated method stub
		return dao.getDetail(id);
	}

	@Override
	public Vacate updateVacateInfo(Vacate Vacate) {
		// TODO Auto-generated method stub
		return dao.updateVacateInfo(Vacate);
	}


	@Override
	public List<Vacate> getVacate1() {
		// TODO Auto-generated method stub
		return dao.getVacate1();
	}

	@Override
	public String deleteVacateInfo(List<String> isFalse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageBean<Vacate> getRecords(PageBean<Vacate> pageBean, String name, String userId,
			String type) {
		int totalrecords =  dao.getAllRecordCounts(pageBean, name,userId,type);
		pageBean.setTotalrecords(totalrecords);
		List<Vacate> result =  dao.getAllRecords(pageBean, name,userId,type);
		pageBean.setResult(result);
		return pageBean;
	}
	
	@Override
	public PageBean<Vacate> getRecord(PageBean<Vacate> pageBean,String startTime,String endTime) {
		int totalrecords =  dao.getAllRecordCount(pageBean, startTime,endTime);
		pageBean.setTotalrecords(totalrecords);
		List<Vacate> result =  dao.getAllRecord(pageBean, startTime,endTime);
		pageBean.setResult(result);
		return pageBean;
	}

	@Override
	public List<Department> getDepartment(String id) {
		// TODO Auto-generated method stub
		return dao.getDepartment(id);
	}
	
	@Override
	public List<User> getUser(String id) {
		// TODO Auto-generated method stub
		return dao.getUser(id);
	}
	
	
	/**
	 * Description:查询字典code对应的字典名称
	 * 作者 : 蒋晨   
	 * 时间 : 2017-11-11 下午1:40:10
	 */
	@Override
	public DataDictionary getNameList(String wgtl){
		return dao.getNameList(wgtl);
	}

	@Override
	public String approverVacate(String id, String opinion,String state,String type) {
		dao.approverVacate(id,opinion,state,type);
		String message = "true";
		return message;
	}

	@Override
	public List<Vacate> getVacateList(String id,String startTime,String endTime) {
		// TODO Auto-generated method stub
		return dao.getVacateList(id,startTime,endTime);
	}

	@Override
	public int getCount(String id,String startTime,String endTime) {
		// TODO Auto-generated method stub
		return dao.getCount(id,startTime,endTime);
	}
	
	
	@Override
	public List<Vacate> getVacateList1(String id,String startTime,String endTime) {
		// TODO Auto-generated method stub
		return dao.getVacateList1(id,startTime,endTime);
	}
}
