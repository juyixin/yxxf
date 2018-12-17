package com.eazytec.crm.service;

import java.util.List;

import com.eazytec.crm.model.Record;
import com.eazytec.util.PageBean;

public interface RecordService {

	List<Record> getRecords(String num);
	
	PageBean<Record> getRecords(PageBean<Record> pageBean,String num,String orderName,String orderType);

	Record saveRecord(Record record);
	
	Record updateRecord(Record record);

	String removeRecords(List<String> recordIdList);

	Record getRecordById(String id);

}
