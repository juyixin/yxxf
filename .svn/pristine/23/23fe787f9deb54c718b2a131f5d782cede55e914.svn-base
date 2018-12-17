package com.eazytec.generalProcess.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.generalProcess.dao.GeneralProcessDao;
import com.eazytec.generalProcess.model.GeneralProcess;
import com.eazytec.generalProcess.service.GeneralProcessService;
import com.eazytec.util.PageBean;

@Service
public class GeneralProcessServiceImpl implements GeneralProcessService{
	
	@Autowired
	private GeneralProcessDao dao;
	

	@Override
	public List<GeneralProcess> getGeneralProcess() {
		// TODO Auto-generated method stub
		return dao.getGeneralProcess();
	}

	@Override
	public List<GeneralProcess> searchGeneralProcess(String name,String state) {
		// TODO Auto-generated method stub
		return dao.searchGeneralProcess(name,state);
	}

	@Override
	public GeneralProcess saveGeneralProcessInfo(GeneralProcess GeneralProcess) {
		// TODO Auto-generated method stub
		return dao.save(GeneralProcess);
	}

	@Override
	public GeneralProcess getDetail(String id) {
		// TODO Auto-generated method stub
		return dao.getDetail(id);
	}

	@Override
	public GeneralProcess updateGeneralProcessInfo(GeneralProcess GeneralProcess) {
		// TODO Auto-generated method stub
		return dao.updateGeneralProcessInfo(GeneralProcess);
	}


	@Override
	public List<GeneralProcess> getGeneralProcess1() {
		// TODO Auto-generated method stub
		return dao.getGeneralProcess1();
	}

	@Override
	public String deleteGeneralProcessInfo(List<String> isFalse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageBean<GeneralProcess> getRecords(PageBean<GeneralProcess> pageBean, String name, String userId,
			String type,String state) {
		int totalrecords =  dao.getAllRecordCount(pageBean, name,userId,type,state);
		pageBean.setTotalrecords(totalrecords);
		List<GeneralProcess> result =  dao.getAllRecord(pageBean, name,userId,type,state);
		pageBean.setResult(result);
		return pageBean;
	}
	
	
}
