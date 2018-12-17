package com.eazytec.crm.dao;

import java.util.List;

import com.eazytec.crm.model.Chance;

import com.eazytec.dao.GenericDao;


public interface ChanceDao extends GenericDao<Chance, String> {

	List<Chance> getAllChanceList(String customerName);
	
	Chance saveChance(Chance chance);
	
	List<Chance> getChancesByIds(List<String> chanceIds);

	void deleteChance(Chance chance);
}