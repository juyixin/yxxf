package com.eazytec.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.crm.dao.RecordDao;
import com.eazytec.crm.model.Record;
import com.eazytec.crm.service.RecordService;
import com.eazytec.dao.hibernate.Updater;
import com.eazytec.util.PageBean;

@Service("recordService")
public class RecordServiceImpl implements RecordService{
	
	@Autowired
	private RecordDao recordDao;
	
	public List<Record> getRecords(String num){
		return recordDao.getAllRecordList(num);
	}
	
	public PageBean<Record> getRecords(PageBean<Record> pageBean,String num,String orderName,String orderType){
		int totalrecords =  recordDao.getAllRecordCount(pageBean, num);
		pageBean.setTotalrecords(totalrecords);
		List<Record> result =  recordDao.getAllRecord(pageBean, num, orderName, orderType);
		pageBean.setResult(result);
		return pageBean;
	}
	
	public Record saveRecord(Record record){
		return recordDao.saveRecord(record);
	}
	
	public Record updateRecord(Record record){
		Updater<Record> updater = new Updater<Record>(record);
		return recordDao.updateByUpdater(updater);
	}
	
	public String removeRecords(List<String> recordIds){
		List<Record> recordList=recordDao.getRecordsByIds(recordIds);
		
		for (Record record : recordList) {
			recordDao.deleteRecord(record);
		}
		return null;
	}
	
	public Record getRecordById(String id){
		return recordDao.get(id);
	}
}
