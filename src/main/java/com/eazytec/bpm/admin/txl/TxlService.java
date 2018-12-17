package com.eazytec.bpm.admin.txl;

import java.util.List;


import javax.jws.WebService;

import com.eazytec.model.NoticePlat;
import com.eazytec.model.Txl;

@WebService
public interface TxlService {
	public List<Txl> getAllTxl();
	
	public List<Txl> getSjCode(String parentCode);
	
	public List<Txl> getBm(String bmdm);
	
	public List<Txl> getTxlTeam(String sjbmdm);
	
	public Txl saveOrUpdateTxlFrom(Txl txlFrom);
	
	public List<Txl> getTxlId(String id);
	
	public void delId(List<String> jyxIds);
	
	public Txl getTxlById(String id);
	
	public List<Txl> getTxljkById(String sjbmdm);
	
	public Txl getId1(String departmentCode);

}
