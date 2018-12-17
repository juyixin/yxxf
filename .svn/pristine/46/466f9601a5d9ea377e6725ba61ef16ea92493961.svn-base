package com.eazytec.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eazytec.crm.dao.ChanceDao;
import com.eazytec.crm.model.Chance;
import com.eazytec.crm.model.Record;
import com.eazytec.crm.service.ChanceService;

@Service("chanceService")
public  class ChanceServiceImpl implements ChanceService{
	
	@Autowired
	private ChanceDao chanceDao;
	
	public List<Chance> getChances(String customerName){
		return chanceDao.getAllChanceList(customerName);
	}
	
	public Chance saveChance(Chance chance){
		return chanceDao.saveChance(chance);
}
	public String removeChances(List<String> chanceIds){
		List<Chance> chanceList=chanceDao.getChancesByIds(chanceIds);
		
		for (Chance chance : chanceList) {
			chanceDao.deleteChance(chance);
		}
		return null;
	}

	
	public Chance getChanceById(String id) {
		return chanceDao.get(id);
	}
}
