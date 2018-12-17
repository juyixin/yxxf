package com.eazytec.bpm.admin.txlry;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.eazytec.model.Txl;
import com.eazytec.model.Txlry;

public interface TxlryService {

	 public List<Txlry> getAllTxlry(String name);
	 
	 public List<Txlry> getAllTxlry1();
	 
	 public List<Txlry> getAllTxlrybm(String name);
	 
	 public Txlry getTxlryById(String id);
	 
	 public List<Txlry> getryBm(String bmdm);
	 
	 public List<Txlry> getTxlryTeam(String sjbmdm);
	 
	 public Txlry saveOrUpdateTxlryFrom(Txlry txlryFrom);
	 
	 public void delryId(List<String> jyxIds);
	 
	 public List<Txlry> getTxlryByIds(String id);
	 
	 public List<Object> readExcelData(XSSFSheet sheetDept, XSSFFormulaEvaluator excute,String department,String departmentCode,String parentCode);
	   
	 public List<Object> readExcelDataFor03(HSSFSheet sheetDept,HSSFFormulaEvaluator excute,String department,String departmentCode,String parentCode);
	 
	 public int count(String departmentCode,String phone);
	 
	 public String saveConsCompanyInfoByList(ArrayList<Txlry> list);
	   
	 public String saveConsCompanyInfoByList1(ArrayList<Txlry> list);
	 
	 public Txlry getId(String departmentCode,String phone);
	 
	

}
