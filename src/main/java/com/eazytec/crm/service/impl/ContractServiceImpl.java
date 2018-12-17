package com.eazytec.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.crm.dao.ContractDao;
import com.eazytec.crm.model.Contract;

import com.eazytec.crm.service.ContractService;
import com.eazytec.util.PageBean;

@Service("contractService")
public class ContractServiceImpl implements ContractService{
	
	@Autowired
	private ContractDao contractDao;
	
	public List<Contract> getContracts(String title){
		return contractDao.getAllContractList(title);
	}
	
	public PageBean<Contract> getContracts(PageBean<Contract> pageBean){
		int totalcontracts =  contractDao.getAllContractCount(pageBean);
		pageBean.setTotalrecords(totalcontracts);
		List<Contract> result =  contractDao.getAllContract(pageBean);
		pageBean.setResult(result);
		return pageBean;
	}
	
	public Contract saveContract(Contract contract){
		return contractDao.saveContract(contract);
	}

	public String removeContracts(List<String> contractIds){
		List<Contract> contractList=contractDao.getContractsByIds(contractIds);
		
		for (Contract contract : contractList) {
			contractDao.deleteContract(contract);
		}
		return null;
	}
	public Contract getContractById(String id){
		return contractDao.get(id);
	}
}

