package com.eazytec.bpm.oa.message.service;

import com.eazytec.model.Message;
import com.eazytec.model.MessageFile;
import com.eazytec.util.PageBean;

public interface MessageService {

	void saveMessage(Message m);

	PageBean<Message> getAllSendBox(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser);

	boolean deleteSendM(String ids);

	Message getMessageById(String id);
	
	MessageFile getMessageFileById(String mid);

	PageBean<Message> getAllDraft(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser);

	void deleteDraft(String id);

	PageBean<Message> getAllInbox(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser);

	boolean deleteInBox(String ids);

	int getAllUnReadMessageCount(String toUser);

	void updateIsRead(String id);

	void deleteMessageFileById(String mid);
}
