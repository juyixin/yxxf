package com.eazytec.bpm.admin.txlry;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Txl;
import com.eazytec.model.Txlry;

@Repository("txlryDao")
public class TxlryDaoImpl extends GenericDaoHibernate<Txlry, String> implements TxlryDao {

	
	public TxlryDaoImpl() {
		super(Txlry.class);
	}

	@Transactional
	@Override
	public List<Txlry> getAllTxlry(String name) {
		// TODO Auto-generated method stub
				String newquery="";
				newquery="select ID as id, XM as xm,BM as bm,DH as dh,TELE as tele,BZ as bz,BMDM as bmdm,SJBMDM as sjbmdm from ETEC_YXXF_TXLRY where type='1' ORDER BY px ASC";
				List<Txlry> noticeplats = getSession().createSQLQuery(newquery)
						.addScalar("id",StandardBasicTypes.STRING)
						.addScalar("xm",StandardBasicTypes.STRING)
						.addScalar("bm",StandardBasicTypes.STRING)
						.addScalar("dh",StandardBasicTypes.STRING)
						.addScalar("tele",StandardBasicTypes.STRING)
						.addScalar("bz",StandardBasicTypes.STRING)
						.addScalar("bmdm",StandardBasicTypes.STRING)
						.addScalar("sjbmdm",StandardBasicTypes.STRING)
						.setResultTransformer(Transformers.aliasToBean(Txlry.class)).list();
				if (noticeplats.isEmpty()) {
					return null;
				} else {
					return noticeplats;
				}
			}
	
	
	@Transactional
	@Override
	public List<Txlry> getAllTxlry1() {
		String sql = "select * from ETEC_YXXF_TXLRY order by px ASC";
		List<Txlry> unitUnions = getSession().createSQLQuery(sql).addEntity(Txlry.class).list();
		if (unitUnions.isEmpty()) {
			return null;
		} else {
			return unitUnions;
		}
	}
	
	@Transactional
	@Override
	public List<Txlry> getAllTxlrybm(String name) {
		// TODO Auto-generated method stub
				String newquery="";
				newquery="select ID as id, XM as xm,BM as bm,DH as dh,TELE as tele,BZ as bz,BMDM as bmdm,SJBMDM as sjbmdm from ETEC_YXXF_TXLRY where sjbmdm='"+name+"' ORDER BY px ASC ";
				List<Txlry> noticeplats = getSession().createSQLQuery(newquery)
						.addScalar("id",StandardBasicTypes.STRING)
						.addScalar("xm",StandardBasicTypes.STRING)
						.addScalar("bm",StandardBasicTypes.STRING)
						.addScalar("dh",StandardBasicTypes.STRING)
						.addScalar("tele",StandardBasicTypes.STRING)
						.addScalar("bz",StandardBasicTypes.STRING)
						.addScalar("bmdm",StandardBasicTypes.STRING)
						.addScalar("sjbmdm",StandardBasicTypes.STRING)
						.setResultTransformer(Transformers.aliasToBean(Txlry.class)).list();
				if (noticeplats.isEmpty()) {
					return null;
				} else {
					return noticeplats;
				}
			}

	 @Transactional
		@Override
		public Txlry getTxlryById(String id) {
				List noticeplats = getSession().createCriteria(Txlry.class)
						.add(Restrictions.eq("id", id)).list();
				if (noticeplats.isEmpty()) {
					return null;
				} else {
					return (Txlry) noticeplats.get(0);
				}
			}
	 
	 
	 @Transactional
		@Override
		public List<Txlry> getryBm(String bmdm) {
			String sql = "select * from ETEC_YXXF_TXLRY t where t.bmdm ='"+bmdm+"' ORDER BY px ASC";
			List<Txlry> unitUnions = getSession().createSQLQuery(sql).addEntity(Txlry.class).list();
			if (unitUnions.isEmpty()) {
				return null;
			} else {
				return unitUnions;
			}
		}
	 
	 
	 @Transactional
		@Override
		public List<Txlry> getTxlryTeam(String parentCode) {
			String sql = "select * from ETEC_YXXF_TXLRY t where t.sjbmdm ='"+parentCode+"'ORDER BY px ASC";
			List<Txlry> unitUnions = getSession().createSQLQuery(sql).addEntity(Txlry.class).list();
			if (unitUnions.isEmpty()) {
				return null;
			} else {
				return unitUnions;
			}
		}

	 	@Transactional
	 	@Override
		public Txlry saveOrUpdateTxlryFrom(Txlry txlryFrom) {
			if (log.isDebugEnabled()) {
				log.debug("noticeplat's id: " + txlryFrom.getId());
			}
			getSession().saveOrUpdate(txlryFrom);
			// necessary to throw a DataIntegrityViolation and catch it in
			// DepartmentManager
			getSession().flush();
			return txlryFrom;
		}
	 	
	 	
	 	//删除
	    @Transactional
		@Override
		public void delryId(String jyxId) {
	    	String hql="delete from Txlry t where t.id=?";
	    	Query query=getSession().createQuery(hql);
			query.setString(0, jyxId);
			query.executeUpdate();
			
		}

		@Override
		public List<Txlry> getTxlryByIds(String id) {
			String sql = "select * from ETEC_YXXF_TXLRY t where t.id ='"+id+"' ORDER BY px ASC";
			List<Txlry> unitUnions = getSession().createSQLQuery(sql).addEntity(Txlry.class).list();
			if (unitUnions.isEmpty()) {
				return null;
			} else {
				return unitUnions;
			}
		}
		
		@Transactional
		@Override
		public Txlry getId(String departmentCode,String phone) {
			String sql = "select * from ETEC_YXXF_TXLRY  where (bmdm ='"+departmentCode+"'and DH='"+phone+"') ORDER BY px ASC";
			//System.out.println(sql);
			List conscompanyinfos = getSession().createSQLQuery(sql).addEntity(Txlry.class).list();
			
			if (conscompanyinfos.isEmpty()) {
				return null;
			} else {
				return (Txlry) conscompanyinfos.get(0);
			}
		}
		
		@Transactional
		@Override
		public int count(String departmentCode,String phone) {
			 int count=0;
			 String listCount = "select * from ETEC_YXXF_TXLRY  where (bmdm ='"+departmentCode+"'and DH='"+phone+"') ORDER BY px ASC";
			 List<Txlry> personalMessage = getSession().createSQLQuery(listCount).addEntity(Txlry.class).list();
			 if(!personalMessage.isEmpty()){	
				/* if(personalMessage.size()>1){
					 count = 2;
				 }else{
					 count = 1; 
				 }*/
				 count = 1; 
			 }else{
				 count = 0;
			 }
			return count;
		}
		
		@Transactional
		@Override
		public void savePersonalMessage1(Txlry txlry) {
			getSession().save(txlry);
			getSession().flush();
		}
		
		@Transactional
		@Override
		public void updatePersonalMessage1(Txlry txlry) {
			getSession().update(txlry);
			getSession().flush();
		}	
}
