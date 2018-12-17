package com.eazytec.bpm.admin.txlry;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.model.Txl;
import com.eazytec.model.Txlry;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("txlryService")
public class TxlryServiceImpl extends GenericManagerImpl<Txlry, String> implements TxlryService {
	public TxlryDao txlryDao;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
	private SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat formartYmd = new SimpleDateFormat("yyyy/MM/dd");
	private DecimalFormat df = new DecimalFormat("0");// 格式化数字
	private DecimalFormat num2 = new DecimalFormat("0.00");
	private DecimalFormat num6 = new DecimalFormat("0.000000");
	
	@Autowired
	public void setTxlryDao(TxlryDao txlryDao) {
		this.txlryDao = txlryDao;
	}


	@Override
	public List<Txlry> getAllTxlry(String name) {
		return txlryDao.getAllTxlry(name);
	}
	
	@Override
	public List<Txlry> getAllTxlry1() {
		return txlryDao.getAllTxlry1();
	}
	
	@Override
	public List<Txlry> getAllTxlrybm(String name) {
		return txlryDao.getAllTxlrybm(name);
	}


	@Override
	public Txlry getTxlryById(String id) {
		return txlryDao.getTxlryById(id);
	}


	@Override
	public List<Txlry> getryBm(String bmdm) {
		// TODO Auto-generated method stub
		return txlryDao.getryBm(bmdm);
	}


	@Override
	public List<Txlry> getTxlryTeam(String sjbmdm) {
		// TODO Auto-generated method stub
		return  txlryDao.getTxlryTeam(sjbmdm);
	}


	@Override
	public Txlry saveOrUpdateTxlryFrom(Txlry txlryFrom) {
		// TODO Auto-generated method stub
		return  txlryDao.saveOrUpdateTxlryFrom(txlryFrom);
	}


	@Override
	public void delryId(List<String> jyxIds) {
				for(int i=1;i<jyxIds.size();i++){
					txlryDao.delryId(jyxIds.get(i));
				}
		}	// TODO Auto-generated method stub


	@Override
	public List<Txlry> getTxlryByIds(String id) {
			return txlryDao.getTxlryByIds(id);
		}
	
	
	
	@Override
	public List<Object> readExcelData(XSSFSheet sheetDept, XSSFFormulaEvaluator excute, String department,
			String departmentCode,String parentCode) {
		ArrayList<Object> list = new ArrayList<Object>();
		ArrayList<Object> dataList = new ArrayList<Object>();
		ArrayList<String> errorList = new ArrayList<String>();
		Map<Integer, String> maps = new HashMap<Integer,  String>();//数据源    
		Map<String, Integer>  map = new TreeMap<String,  Integer>();//接收的集合        
		List<String> newList = new ArrayList<String>();
		String errorString = "";
			for (int i = 1; i < sheetDept.getLastRowNum() + 1; i++) {
				System.out.println("**********正在读取第"+i+"行记录**********");
				int index = i + 1;
				Txlry p = new Txlry();
				try {
					p.setType("1");
					p.setBm(department);
					p.setBmdm(departmentCode);
					p.setSjbmdm(parentCode);
					p.setXm((String) getCellValue(
							sheetDept.getRow(i).getCell(0), excute));
					p.setDh((String) getCellValue(
							sheetDept.getRow(i).getCell(1), excute));
					p.setTele((String) getCellValue(
							sheetDept.getRow(i).getCell(2), excute));
				    p.setBz((String) getCellValue(
							sheetDept.getRow(i).getCell(3), excute));
					if (p.getXm() == null) {
						
						errorString = "第" + index + "行;第" + 1
								+ "列数据为空";
						errorList.add(errorString);
					}
                    if (p.getDh() == null) {
						
						errorString = "第" + index + "行;第" + 2
								+ "列数据为空";
						errorList.add(errorString);
                    }
                    if (p.getTele() == null) {
						
						errorString = "第" + index + "行;第" + 3
								+ "列数据为空";
						errorList.add(errorString);
                    }
                    Pattern p1 = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            		Matcher m ;
            		m = p1.matcher(p.getDh());
            		boolean dateFlag1 = m.matches();
            		if (!dateFlag1) {
    						errorString = "第" + index + "行;手机格式错误";
    						errorList.add(errorString);
            		}
            		maps.put(index, p.getDh());
            		if(i==sheetDept.getLastRowNum()){
            			for(Map.Entry<Integer, String> entry : maps.entrySet()){    
							if(map.get(entry.getValue()) != null)     {    
								map.put(entry.getValue(), map.get(entry.getValue()) + 1);  
								if((map.get(entry.getValue())+1)>1){
									newList.add(entry.getValue());
								}
							} else {    
									map.put(entry.getValue(), 1);  
								}     
							}    
						 HashSet h  =   new  HashSet(newList); 
						 newList.clear(); 
						 newList.addAll(h);
						// System.out.println(newList); 
						 int k = 0;
						 for(String s : newList){	
								for(Integer getKey: maps.keySet()){  	
									if(maps.get(getKey).equals(s)){  
										//oldList.add(getKey);  
										errorString = "第" + getKey + "行;手机号重复";
										errorList.add(errorString);
									}  
								}
								errorString = "-------------";
								errorList.add(errorString);
							
						}
					}
            		
				} catch (Exception e) {
					errorString = "第" + index + "行解析报错";
					errorList.add(errorString);
					continue;
				}
				dataList.add(p);
			}
		
		list.add(dataList);
		list.add(errorList);
		return list;
	}



	

	@Override
	public List<Object> readExcelDataFor03(HSSFSheet sheetDept, HSSFFormulaEvaluator excute,
			String department, String departmentCode, String parentCode) {
		ArrayList<Object> list = new ArrayList<Object>();
		ArrayList<Object> dataList = new ArrayList<Object>();
		ArrayList<String> errorList = new ArrayList<String>();
		String errorString = "";
		Map<Integer, String> maps = new HashMap<Integer,  String>();//数据源     
		Map<String, Integer>  map = new TreeMap<String,  Integer>();//接收的集合        
		List<String> newList = new ArrayList<String>();
			for (int i = 1; i < sheetDept.getLastRowNum() + 1; i++) {
				System.out.println("**********正在读取第"+i+"行记录**********");
				int index = i + 1;
				Txlry p = new Txlry();
				try {
					p.setType("1");
					p.setBm(department);
					p.setBmdm(departmentCode);
					p.setSjbmdm(parentCode);
					p.setXm((String) getCellValueFor03(
							sheetDept.getRow(i).getCell(0), excute));
					p.setDh((String) getCellValueFor03(
							sheetDept.getRow(i).getCell(1), excute));
					p.setTele((String) getCellValueFor03(
							sheetDept.getRow(i).getCell(2), excute));
				    p.setBz((String) getCellValueFor03(
							sheetDept.getRow(i).getCell(3), excute));
                    if (p.getXm() == null) {
						
						errorString = "第" + index + "行;第" + 1
								+ "列数据为空";
						errorList.add(errorString);
					}
                    if (p.getDh() == null) {
						
						errorString = "第" + index + "行;第" + 2
								+ "列数据为空";
						errorList.add(errorString);
                    }
                    
                    Pattern p1 = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            		Matcher m ;
            		m = p1.matcher(p.getDh());
            		boolean dateFlag1 = m.matches();
            		if (!dateFlag1) {
    						errorString = "第" + index + "行;手机格式错误";
    						errorList.add(errorString);
            		}
            		maps.put(index, p.getDh());
            		if(i==sheetDept.getLastRowNum()){
            			for(Map.Entry<Integer, String> entry : maps.entrySet()){    
							if(map.get(entry.getValue()) != null)     {    
								map.put(entry.getValue(), map.get(entry.getValue()) + 1);  
								if((map.get(entry.getValue())+1)>1){
									newList.add(entry.getValue());
								}
							} else {    
									map.put(entry.getValue(), 1);  
								}     
							}    
						 HashSet h  =   new  HashSet(newList); 
						 newList.clear(); 
						 newList.addAll(h);
						// System.out.println(newList); 
						 int k = 0;
						 for(String s : newList){	
								for(Integer getKey: maps.keySet()){  	
									if(maps.get(getKey).equals(s)){  
										//oldList.add(getKey);  
										errorString = "第" + getKey + "行;手机号重复";
										errorList.add(errorString);
									}  
								}
								errorString = "-------------";
								errorList.add(errorString);
							
						}
					}
            		
				} catch (Exception e) {
					errorString = "第" + index + "行解析报错";
					errorList.add(errorString);
					continue;
				}
				dataList.add(p);
			}
		list.add(dataList);
		list.add(errorList);
		return list;
	}
	
	public Object getCellValue(XSSFCell cell, XSSFFormulaEvaluator excute) {
		Object data;
		if (cell == null) {
			data = null;
			return data;
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			if (cell.getStringCellValue().equals("")) {
				data = null;
			} else {
				data = cell.getStringCellValue();
			}

			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			if ("@".equals(cell.getCellStyle().getDataFormatString())) {
				data = df.format(cell.getNumericCellValue());
			} else if ("General".equals(cell.getCellStyle()
					.getDataFormatString())) {
				data = df.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString()
							.startsWith("0.00_")) {
				data = num2.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString().equals("0.00")) {
				data = num2.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString()
							.startsWith("0.000000_")) {
				data = num6.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString()
							.startsWith("0_")) {
				data = df.format(cell.getNumericCellValue());
			} else {
				data = sdfYmd.format(HSSFDateUtil.getJavaDate(cell
						.getNumericCellValue()));
			}
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			data = cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			data = excute.evaluate(cell).getStringValue();
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			data = null;
			break;
		default:
			data = cell.toString();
			break;
		}
		return data;
	}

	public Object getCellValueFor03(HSSFCell cell, HSSFFormulaEvaluator excute) {
		Object data;
		if (cell == null) {
			data = null;
			return data;
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			if (cell.getStringCellValue().equals("")) {
				data = null;
			} else {
				data = cell.getStringCellValue();
			}

			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if ("@".equals(cell.getCellStyle().getDataFormatString())) {
				data = df.format(cell.getNumericCellValue());
			} else if ("General".equals(cell.getCellStyle()
					.getDataFormatString())) {
				data = df.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString()
							.startsWith("0.00_")) {
				data = num2.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString().equals("0.00")) {
				data = num2.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString()
							.startsWith("0.000000_")) {
				data = num6.format(cell.getNumericCellValue());
			} else if (cell.getCellStyle().getDataFormatString() != null
					&& cell.getCellStyle().getDataFormatString()
							.startsWith("0_")) {
				data = df.format(cell.getNumericCellValue());
			} else {
				data = sdfYmd.format(HSSFDateUtil.getJavaDate(cell
						.getNumericCellValue()));
			}
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			data = cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			data = excute.evaluate(cell).getStringValue();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			data = null;
			break;
		default:
			data = cell.toString();
			break;
		}
		return data;
	}


	@Override
	public int count(String departmentCode,String phone) {
		return txlryDao.count(departmentCode,phone);
	}


	@Override
	public String saveConsCompanyInfoByList(ArrayList<Txlry> list) {
		String message="";
		try{
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for(int i=0;i<list.size();i++){
				System.out.println("**********保存人员记录："+i+"**********" + currentTime.toGMTString());
				txlryDao.savePersonalMessage1(list.get(i));
			}
			message="true";
		}catch (Exception e){
			e.printStackTrace();
			log.warn(e.getMessage());
			message="false";
		}
		return message;
	}


	@Override
	public String saveConsCompanyInfoByList1(ArrayList<Txlry> list) {
		String message="";
		try{
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for(int i=0;i<list.size();i++){
				System.out.println("**********更新人员记录："+i+"**********" + currentTime.toGMTString());
				txlryDao.updatePersonalMessage1(list.get(i));
			}
			message="true";
		}catch (Exception e){
			e.printStackTrace();
			log.warn(e.getMessage());
			message="false";
		}
		return message;
	}


	@Override
	public Txlry getId(String departmentCode,String phone) {
		return txlryDao.getId(departmentCode,phone);
	}
	

}

