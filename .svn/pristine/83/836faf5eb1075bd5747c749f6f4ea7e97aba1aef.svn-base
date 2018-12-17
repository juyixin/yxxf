package com.eazytec.bpm.admin.Zcfg;

import java.util.List;

import com.eazytec.crm.model.Record;
import com.eazytec.dao.GenericDao;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Zcfg;
import com.eazytec.util.PageBean;

public interface ZcfgDao extends GenericDao<Zcfg,java.lang.String>{
	
	public List<Zcfg> getZcfgListByUserid();

	void removeZcfg(String jyxId);
	
	public Zcfg saveZcfg(Zcfg zcfg);
	
	public Zcfg getZcfgById(String id);
	
	
	int getAllZcfgCount(PageBean<Zcfg> pageBean,String saerchtext);

	List<Zcfg> getAllZcfg(PageBean<Zcfg> pageBean,String saerchtext);
	
	public List<Zcfg> getZcfgListByserach(String name);
}
