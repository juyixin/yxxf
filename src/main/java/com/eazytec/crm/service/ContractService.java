package com.eazytec.crm.service;

import java.util.List;

import com.eazytec.crm.model.Contract;

import com.eazytec.util.PageBean;



public interface ContractService {

	List<Contract> getContracts(String title);
	
	PageBean<Contract> getContracts(PageBean<Contract> pageBean);
	
	Contract saveContract(Contract contract);
	
	String removeContracts(List<String> contractIdList);
	
	Contract getContractById(String id);
}
