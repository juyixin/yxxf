package com.eazytec.bpm.admin.txl;

import java.util.ArrayList;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Txl;

@Repository("txlDao")
public class TxlDaoImpl extends GenericDaoHibernate<Txl, String> implements TxlDao {

	public TxlDaoImpl() {
		super(Txl.class);
	}
	
	@Transactional
	@Override
	public List<Txl> getAllTxl() {
		String sql = "select ID as id,BM as bm ,SJBMDM as sjbmdm,BMDM as bmdm,BZ as bz  from ETEC_YXXF_TXL where bmdm !='Organization'";
		List<Txl> lit=new ArrayList();
		try{
		 lit = getSession().createSQLQuery(sql)
				.addScalar("id",StandardBasicTypes.STRING)
				.addScalar("bm",StandardBasicTypes.STRING)
				.addScalar("sjbmdm",StandardBasicTypes.STRING)
				.addScalar("bmdm",StandardBasicTypes.STRING)
				.addScalar("bz",StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(Txl.class)).list();

		 }catch(Exception e){
			 e.printStackTrace();
		 }	
		return lit;	
	}

	@Transactional
	@Override
	public List<Txl> getSjCode(String parentCode) {
		String sql = "select ID as id,BM as bm ,SJBMDM as sjbmdm,BMDM as bmdm,BZ as bz  from ETEC_YXXF_TXL where sjbmdm ='"+parentCode+"'";
		List<Txl> lit=new ArrayList();
    	 lit = getSession().createSQLQuery(sql)
    			.addScalar("id",StandardBasicTypes.STRING)
				.addScalar("bm",StandardBasicTypes.STRING)
				.addScalar("sjbmdm",StandardBasicTypes.STRING)
				.addScalar("bmdm",StandardBasicTypes.STRING)
				.addScalar("bz",StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(Txl.class)).list();
		if (lit.isEmpty()) {
			return null;
		} else {
			return lit;
		}
	}
	
	@Transactional
	@Override
	public List<Txl> getTxlTeam(String parentCode) {
		String sql = "select * from ETEC_YXXF_TXL t where t.sjbmdm ='"+parentCode+"' ORDER BY bmdm ASC";
		List<Txl> unitUnions = getSession().createSQLQuery(sql).addEntity(Txl.class).list();
		if (unitUnions.isEmpty()) {
			return null;
		} else {
			return unitUnions;
		}
	}
	@Transactional
	@Override
	public Txl saveOrUpdateTxlFrom(Txl txlFrom) {
		if (log.isDebugEnabled()) {
			log.debug("noticeplat's id: " + txlFrom.getId());
		}
		getSession().saveOrUpdate(txlFrom);
		// necessary to throw a DataIntegrityViolation and catch it in
		// DepartmentManager
		getSession().flush();
		return txlFrom;
	}
	
	@Transactional
	@Override
	public List<Txl> getBm(String bmdm) {
		String sql = "select * from ETEC_YXXF_TXL t where t.bmdm ='"+bmdm+"'";
		List<Txl> unitUnions = getSession().createSQLQuery(sql).addEntity(Txl.class).list();
		if (unitUnions.isEmpty()) {
			return null;
		} else {
			return unitUnions;
		}
	}
	//删除
    @Transactional
	@Override
	public void delId(String jyxId) {
		String hql="delete from Txl t where t.id=?";
		Query query=getSession().createQuery(hql);
		query.setString(0, jyxId);
		query.executeUpdate();
	}
    
    @Transactional
	@Override
	public List<Txl> getTxlId(String id) {
    	String sql="";
    	if(id==null||id==""){
    		sql = "select * from ETEC_YXXF_TXL t where t.id ='Organization'";
    	}else{
		 sql = "select * from ETEC_YXXF_TXL t where t.id ='"+id+"'";
    	}
		List<Txl> unitUnions = getSession().createSQLQuery(sql).addEntity(Txl.class).list();
		if (unitUnions.isEmpty()) {
			return null;
		} else {
			return unitUnions;
		}
	}

    @Transactional
	@Override
	public Txl getTxlById(String id) {
			List noticeplats = getSession().createCriteria(Txl.class)
					.add(Restrictions.eq("id", id)).list();
			if (noticeplats.isEmpty()) {
				return null;
			} else {
				return (Txl) noticeplats.get(0);
			}
		}

    @Transactional
   	@Override
	public List<Txl> getTxljkById(String sjbmdm) {
    	String sql = "select * from ETEC_YXXF_TXL t where t.sjbmdm ='"+sjbmdm+"'";
    	List<Txl> unitUnions =getSession().createSQLQuery(sql).addEntity(Txl.class).list();
		if (unitUnions.isEmpty()) {
			return null;
		} else {
			return unitUnions;
		}
	}

    @Transactional
	@Override
	public Txl getId1(String departmentCode) {
		String sql = "select * from ETEC_YXXF_TXL  where bmdm ='"+departmentCode+"'";
		//System.out.println(sql);
		List conscompanyinfos = getSession().createSQLQuery(sql).addEntity(Txl.class).list();
		
		if (conscompanyinfos.isEmpty()) {
			return null;
		} else {
			return (Txl) conscompanyinfos.get(0);
		}
	}
    

}



