package com.eazytec.crm.dao;

import java.util.List;

import com.eazytec.crm.model.Telreg;
import com.eazytec.dao.GenericDao;

public interface TelregDao extends GenericDao<Telreg, String>{
	List<Telreg> getAlltelregList(String num, String telnum);
	
	Telreg saveTelreg(Telreg telreg);
	
	List<Telreg> getTelregsByIds(List<String> telregIds);

	void deleteTelreg(Telreg telreg);

}
