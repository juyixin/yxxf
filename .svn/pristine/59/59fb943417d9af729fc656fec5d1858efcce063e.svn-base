package com.eazytec.crm.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.crm.dao.TelregDao;
import com.eazytec.crm.model.Record;
import com.eazytec.crm.model.Telreg;
import com.eazytec.crm.service.TelregService;

@Service("telregService")
public class TelregServiceImpl implements TelregService{
	
	@Autowired
	private TelregDao telregDao;
	

	@Override
	public List<Telreg> getTelregs(String num,String telnum) {
		return telregDao.getAlltelregList(num,telnum);
		}


	@Override
	public Telreg saveTelreg(Telreg telreg) {
		return telregDao.saveTelreg(telreg);
	}


	@Override
	public String removeTelregs(List<String> telregIds) {
		List<Telreg> telregList=telregDao.getTelregsByIds(telregIds);
		
		for (Telreg telreg : telregList) {
			telregDao.deleteTelreg(telreg);
		}
		return null;
	}


	@Override
	public Telreg getTelregById(String id) {
		return telregDao.get(id);	}
}