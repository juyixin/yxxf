package com.eazytec.bpm.oa.message.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.oa.message.dao.MessageDao;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Message;
import com.eazytec.model.MessageFile;
import com.eazytec.util.PageBean;

@Repository("messageDao")
public class MessageDaoImpl extends GenericDaoHibernate<Message, String> implements MessageDao {
	public MessageDaoImpl() {
		super(Message.class);
	}

	@Override
	public List<Message> getAllSendM(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser) {
		String hql = "from Message m where 1=1";
		hql += " and m.fromUser='" + fromUser + "' and m.isDraft=0 and fromStatus=1";
		if (!StringUtil.isEmptyString(ordername)) {
			hql += " order by m." + ordername + " ";
		}
		if (!StringUtil.isEmptyString(ordertype)) {
			hql += ordertype;
		}
		hql += " order by m.messageDate desc";
		Query qry = getSession().createQuery(hql);
		qry.setFirstResult(pageBean.getStartRow()); // 从多少条记录开始取（从0开始）
		qry.setMaxResults(pageBean.getPageSize()); // 取多少条记录
		List<Message> list = qry.list();
		return list;
	}

	@Override
	public int getAllSendMCoutn(String fromUser) {
		String hql = "select count(*) from Message m where 1=1";
		hql += " and m.fromUser='" + fromUser + "' and m.isDraft=0 and fromStatus=1";
		int r = ((Long) getSession().createQuery(hql).uniqueResult()).intValue();
		return r;
	}

	@Override
	public boolean deleteSendM(String ids) {
		String sql = "update etec_message set from_status=0 where id='" + ids + "'";
		Query qry = getSession().createSQLQuery(sql);
		int result = qry.executeUpdate();
		return result != 0 ? true : false;
	}

	@Override
	public Message getMessageById(String id) {
		String hql = "from Message m where m.id='" + id + "'";
		List<Message> list = getSession().createQuery(hql).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public MessageFile getMessageFileById(String mid) {
		String hql = "from MessageFile m where m.id='" + mid + "'";
		List<MessageFile> list = getSession().createQuery(hql).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Message> getAllDraft(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser) {
		String hql = "from Message m where 1=1";
		hql += " and m.fromUser='" + fromUser + "' and m.isDraft=1 and fromStatus=1";
		if (!StringUtil.isEmptyString(ordername)) {
			hql += " order by m." + ordername + " ";
		}
		if (!StringUtil.isEmptyString(ordertype)) {
			hql += ordertype;
		}
		hql += " order by m.messageDate desc";
		Query qry = getSession().createQuery(hql);
		qry.setFirstResult(pageBean.getStartRow()); // 从多少条记录开始取（从0开始）
		qry.setMaxResults(pageBean.getPageSize()); // 取多少条记录
		List<Message> list = qry.list();
		return list;
	}

	@Override
	public int getAllDraftCoutn(String fromUser) {
		String hql = "select count(*) from Message m where 1=1";
		hql += " and m.fromUser='" + fromUser + "' and m.isDraft=1 and fromStatus=1";
		int r = ((Long) getSession().createQuery(hql).uniqueResult()).intValue();
		return r;
	}

	@Override
	public List<Message> getAllInbox(PageBean<Message> pageBean, String ordername, String ordertype, String fromUser) {
		String hql = "from Message m where 1=1";
		hql += " and m.toUser like '%" + fromUser + "%' and m.isDraft=0 and toStatus=1";
		if (!StringUtil.isEmptyString(ordername)) {
			hql += " order by m." + ordername + " ";
		}
		if (!StringUtil.isEmptyString(ordertype)) {
			hql += ordertype;
		}
		hql += " order by m.messageDate desc";
		Query qry = getSession().createQuery(hql);
		qry.setFirstResult(pageBean.getStartRow()); // 从多少条记录开始取（从0开始）
		qry.setMaxResults(pageBean.getPageSize()); // 取多少条记录
		List<Message> list = qry.list();
		return list;
	}

	@Override
	public int getAllInboxCoutn(String fromUser) {
		String hql = "select count(*) from Message m where 1=1";
		hql += " and m.toUser like '%" + fromUser + "%' and m.isDraft=0 and toStatus=1";
		int r = ((Long) getSession().createQuery(hql).uniqueResult()).intValue();
		return r;
	}

	@Override
	public boolean deleteInBox(String ids) {
		String sql = "update etec_message set to_status=0 where id='" + ids + "'";
		Query qry = getSession().createSQLQuery(sql);
		int result = qry.executeUpdate();
		return result != 0 ? true : false;
	}

	@Override
	public int getAllUnReadMessageCount(String toUser) {
		String hql = "select count(*) from Message m where m.toUser like '%" + toUser + "%' and m.isRead=0 and m.toStatus=1";
		int r = ((Long) getSession().createQuery(hql).uniqueResult()).intValue();
		return r;
	}

	@Override
	public void updateIsRead(String id) {
		String sql = "update etec_message set is_read=1 where id='" + id + "'";
		Query qry = getSession().createSQLQuery(sql);
		qry.executeUpdate();
	}

	@Override
	public void deleteMessageFileById(String mid) {
		String sql = "delete from MessageFile mf where mf.id = '" + mid + "'";
		Query qry = getSession().createQuery(sql);
		qry.executeUpdate();
	}
}
