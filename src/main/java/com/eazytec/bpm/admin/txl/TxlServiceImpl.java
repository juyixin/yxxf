package com.eazytec.bpm.admin.txl;

import java.util.List;


import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eazytec.model.Txl;
import com.eazytec.service.impl.GenericManagerImpl;


@Service("txlService")
public class TxlServiceImpl  extends GenericManagerImpl<Txl, String> implements TxlService{
	
	private TxlDao txlDao;
	@Autowired
	public TxlServiceImpl(TxlDao txlDao) {
		super(txlDao);
		this.txlDao = txlDao;
	}
	
	@Autowired
	public void setTxlDao(TxlDao txlDao) {
		this.txlDao = txlDao;
	}


	@Override
	public List<Txl> getAllTxl() {
		return txlDao.getAllTxl();
    }
	
	@Override
	public List<Txl> getSjCode(String parentCode) {
		// TODO Auto-generated method stub
		return txlDao.getSjCode(parentCode);
	}
	
	@Override
	public List<Txl> getTxlTeam(String sjbmdm) {
		// TODO Auto-generated method stub
		return txlDao.getTxlTeam(sjbmdm);
	}

	@Override
	public Txl saveOrUpdateTxlFrom(Txl txlFrom) {
		// TODO Auto-generated method stub
		return txlDao.saveOrUpdateTxlFrom(txlFrom);
	}

	@Override
	public List<Txl> getBm(String bmdm) {
		// TODO Auto-generated method stub
		return txlDao.getBm(bmdm);
	}

	
	@Override
	public List<Txl> getTxlId(String id) {
		// TODO Auto-generated method stub
		return txlDao.getTxlId(id);
	}

	@Override
	public void delId(List<String> jyxIds) {
			for(int i=1;i<jyxIds.size();i++){
				txlDao.delId(jyxIds.get(i));
			}
	}

	@Override
	public Txl getTxlById(String id) {
		return txlDao.getTxlById(id);
	}

	@Override
	public List<Txl> getTxljkById(String sjbmdm) {
		return txlDao.getTxljkById(sjbmdm);
	}
	
	
	@Override
	public Txl getId1(String departmentCode) {
		return txlDao.getId1(departmentCode);
	}
		
	
}
