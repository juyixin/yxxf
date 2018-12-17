package com.eazytec.crm.dao;

import java.util.List;

import com.eazytec.crm.model.Contract;

import com.eazytec.dao.GenericDao;
import com.eazytec.util.PageBean;

public interface ContractDao extends GenericDao<Contract, String> {

	List<Contract> getAllContractList(String title);
	
	Contract saveContract(Contract contract);
	
	List<Contract> getContractsByIds(List<String> contractIds);

	void deleteContract(Contract contract);
	
	int getAllContractCount(PageBean<Contract> pageBean);

	List<Contract> getAllContract(PageBean<Contract> pageBean);
	
}
