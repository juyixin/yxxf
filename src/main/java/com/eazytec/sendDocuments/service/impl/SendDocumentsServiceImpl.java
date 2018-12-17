package com.eazytec.sendDocuments.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.sendDocuments.dao.SendDocumentsDao;
import com.eazytec.sendDocuments.model.SendDocuments;
import com.eazytec.sendDocuments.service.SendDocumentsService;
import com.eazytec.util.PageBean;


@Service
public class SendDocumentsServiceImpl implements SendDocumentsService{
	
	@Autowired
	private SendDocumentsDao dao;
	

	@Override
	public List<SendDocuments> getSendDocuments() {
		// TODO Auto-generated method stub
		return dao.getSendDocuments();
	}

	@Override
	public List<SendDocuments> searchSendDocuments(String name) {
		// TODO Auto-generated method stub
		return dao.searchSendDocuments(name);
	}

	@Override
	public SendDocuments saveSendDocumentsInfo(SendDocuments sendDocuments) {
		// TODO Auto-generated method stub
		return dao.save(sendDocuments);
	}

	@Override
	public SendDocuments getDetail(String id) {
		// TODO Auto-generated method stub
		return dao.getDetail(id);
	}

	@Override
	public SendDocuments updateSendDocumentsInfo(SendDocuments sendDocuments) {
		// TODO Auto-generated method stub
		return dao.updateSendDocumentsInfo(sendDocuments);
	}


	@Override
	public List<SendDocuments> getSendDocuments1() {
		// TODO Auto-generated method stub
		return dao.getSendDocuments1();
	}

	@Override
	public String deleteSendDocumentsInfo(List<String> isFalse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageBean<SendDocuments> getRecords(PageBean<SendDocuments> pageBean, String name, String userId,
			String type) {
		int totalrecords =  dao.getAllRecordCount(pageBean, name,userId,type);
		pageBean.setTotalrecords(totalrecords);
		List<SendDocuments> result =  dao.getAllRecord(pageBean, name,userId,type);
		pageBean.setResult(result);
		return pageBean;
	}

}
