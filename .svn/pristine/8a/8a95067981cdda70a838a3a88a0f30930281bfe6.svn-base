package com.eazytec.crm.dao;

import java.util.List;

import com.eazytec.crm.model.Record;
import com.eazytec.dao.GenericDao;
import com.eazytec.util.PageBean;

public interface RecordDao extends GenericDao<Record, String> {

	List<Record> getAllRecordList(String num);

	Record saveRecord(Record record);

	List<Record> getRecordsByIds(List<String> recordIds);

	void deleteRecord(Record record);

	int getAllRecordCount(PageBean<Record> pageBean,String num);

	List<Record> getAllRecord(PageBean<Record> pageBean,String num, String orderName,String orderType);
	
}
