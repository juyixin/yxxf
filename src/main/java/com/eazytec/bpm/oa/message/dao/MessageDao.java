package com.eazytec.bpm.oa.message.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.model.Message;
import com.eazytec.model.MessageFile;
import com.eazytec.util.PageBean;

public interface MessageDao extends GenericDao<Message, String>{

	List<Message> getAllSendM(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser); // 获得发件箱信息

	int getAllSendMCoutn(String fromUser); // 获得发件箱总数

	boolean deleteSendM(String ids); // 删除发件人的消息

	Message getMessageById(String id); // 根据id获得消息
	
	MessageFile getMessageFileById(String mid);

	List<Message> getAllDraft(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser); // 获得草稿箱信息

	int getAllDraftCoutn(String fromUser); // 获得草稿箱总数

	List<Message> getAllInbox(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser); // 获得收件箱信息

	int getAllInboxCoutn(String fromUser); // 获得收件箱总数

	boolean deleteInBox(String ids); // 删除收件人的消息

	int getAllUnReadMessageCount(String toUser); // 获得所有未读消息

	void updateIsRead(String id); // 修改消息为已读

	void deleteMessageFileById(String mid);
}
