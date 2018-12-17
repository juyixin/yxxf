package com.eazytec.alxxgl.dao;

import java.util.List;

import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.crm.model.Record;
import com.eazytec.dao.GenericDao;
import com.eazytec.util.PageBean;

public interface AlxxbDao extends GenericDao<Alxxb,String>{

	Alxxb saveAlxxb(Alxxb alxxb);

	List<Alxxb> getAlxxbByIds(List<String> alxxbIds);

	void deleteAlxxb(Alxxb alxxb);

	List<Alxxb> getAllxByAllx(String allx);
	
	List<Alxxb> getAllxByAllx1(String allx);
	
	List<Alxxb> getAllxByAllx2(String id,String name);
 
	Alxxb getAllxByIds(String id);
	
	int getAllRecordCount(PageBean<Alxxb> pageBean,String num,String allx,String dsr);
	
	List<Alxxb> getAllRecord(PageBean<Alxxb> pageBean,String name, String allx,String dsr);
	
	List<Alxxb> getlistsize(String name, String allx,String dsr );
}
