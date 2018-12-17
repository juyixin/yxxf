package com.eazytec.bpm.admin.txl;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Txl;

public interface TxlDao extends GenericDao<Txl, String> {

	public List<Txl> getAllTxl();
	
	public List<Txl> getBm(String bmdm);
	
	public List<Txl> getSjCode(String parentCode);

	public List<Txl> getTxlTeam(String sjbmdm);
	
	public Txl saveOrUpdateTxlFrom(Txl txlFrom);
	
	public List<Txl> getTxlId(String id);
	
	void delId(String jyxId);
	
	public Txl getId1(String departmentCode);
	 
	public Txl getTxlById(String id);
	
	public List<Txl> getTxljkById(String sjbmdm);
	
}
