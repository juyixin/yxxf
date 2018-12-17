package com.eazytec.crm.service;

import java.util.List;

import com.eazytec.crm.model.Chance;
import com.eazytec.crm.model.Record;

public interface ChanceService {

	List<Chance> getChances(String customerName);
	
	Chance saveChance(Chance chance);
	
	String removeChances(List<String> chanceIdList);
	
	Chance getChanceById(String id);
}