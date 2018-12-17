package com.eazytec.fileManage.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.fileManage.dao.FileManageDao;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.link.model.Link;
import com.eazytec.model.NoticeDocument;

@Repository("fileManageDao")
public class FileManageDaoImpl extends GenericDaoHibernate<FileManage,String> implements FileManageDao{
	
	public FileManageDaoImpl(){
		super(FileManage.class);
	}
    
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
 @Transactional
	@Override
	public FileManage saveFileManage(FileManage fileManage) {
		getSession().saveOrUpdate(fileManage);
		getSession().flush();
		return fileManage;
	}
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	@Override
	public void saveFileManage1(FileManage fileManage) {
		getSession().save(fileManage);
		getSession().flush();
	}

    
	
	/**
	  * Description:根据外键fileId查找数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-15  下午13:53:13
	  */
	@Override
	public List<FileManage> getEvidence(String fileId) {
		String sql = "select * from etec_public_file_manage where file_id='"+fileId+"'";
		List<FileManage> fileManageList = getSession().createSQLQuery(sql).addEntity(FileManage.class).list();
		return fileManageList;
	}
	
	
	/**
	  * Description:根据主键Id查找数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-15  下午13:53:13
	  */
	@Override
	public FileManage getDetail(String id) {
		String sql = "select * from etec_public_file_manage where id='"+id+"'";
		FileManage fileManage = (FileManage) getSession().createSQLQuery(sql).addEntity(FileManage.class).setMaxResults(1).uniqueResult();
		return fileManage;
	}


	@Transactional
	@Override
		public void deleteFileManageById(String id) {
			// TODO Auto-generated method stub
			getSession().createSQLQuery("delete from etec_public_file_manage where id = ?")
			.setParameter(0, id).executeUpdate();
		}


	@Transactional
	@Override
	public FileManage getFileManageById(String id) {
			List noticedocuments = getSession().createCriteria(FileManage.class)
					.add(Restrictions.eq("id", id)).list();
			if (noticedocuments.isEmpty()) {
				return null;
			} else {
				return (FileManage) noticedocuments.get(0);
			}
		}
	
	@Transactional
	@Override
	public List<FileManage> getEvidenceids(String fileId) {
		String sql = "select * from etec_public_file_manage t where t.file_id='"+fileId+"'";
		List<FileManage> fileManageList = getSession().createSQLQuery(sql).addEntity(FileManage.class).list();
		return fileManageList;
	}
		
	}

