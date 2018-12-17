package com.eazytec.bpm.oa.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.oa.message.dao.MessageDao;
import com.eazytec.bpm.oa.message.service.MessageService;
import com.eazytec.model.MessageFile;
import com.eazytec.model.Message;
import com.eazytec.util.PageBean;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public void saveMessage(Message m) {
		messageDao.save(m);
	}

	@Override
	public PageBean<Message> getAllSendBox(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser) {
		int totalcounts = messageDao.getAllSendMCoutn(fromUser);
		pageBean.setTotalrecords(totalcounts);
		List<Message> list = messageDao.getAllSendM(pageBean, ordername, ordertype, fromUser);
		pageBean.setResult(list);
		return pageBean;
	}

	@Override
	public boolean deleteSendM(String ids) {
		return messageDao.deleteSendM(ids);
	}

	@Override
	public Message getMessageById(String id) {
		return messageDao.getMessageById(id);
	}

	@Override
	public MessageFile getMessageFileById(String mid){
		return messageDao.getMessageFileById(mid);
	}

	@Override
	public PageBean<Message> getAllDraft(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser) {
		int totalcounts = messageDao.getAllDraftCoutn(fromUser);
		pageBean.setTotalrecords(totalcounts);
		List<Message> list = messageDao.getAllDraft(pageBean, ordername, ordertype, fromUser);
		pageBean.setResult(list);
		return pageBean;
	}

	@Override
	public void deleteDraft(String id) {
		messageDao.remove(id);
	}

	@Override
	public PageBean<Message> getAllInbox(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser) {
		int totalcounts = messageDao.getAllInboxCoutn(fromUser);
		pageBean.setTotalrecords(totalcounts);
		List<Message> list = messageDao.getAllInbox(pageBean, ordername, ordertype, fromUser);
		pageBean.setResult(list);
		return pageBean;
	}

	@Override
	public boolean deleteInBox(String ids) {
		return messageDao.deleteInBox(ids);
	}

	@Override
	public int getAllUnReadMessageCount(String toUser) {
		return messageDao.getAllUnReadMessageCount(toUser);
	}

	@Override
	public void updateIsRead(String id) {
		messageDao.updateIsRead(id);
	}
	
	@Override
	public void deleteMessageFileById(String mid){
		messageDao.deleteMessageFileById(mid);
	}

}
