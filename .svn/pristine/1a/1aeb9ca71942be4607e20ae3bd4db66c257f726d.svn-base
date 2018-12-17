package com.eazytec.bpm.admin.datadictionary.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.datadictionary.dao.DataDictionaryDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.LabelValue;

/**
 * 
 * @author Karthick
 *
 */
@Repository("dataDictionaryDao")
public class DataDictionaryDaoImpl extends GenericDaoHibernate<DataDictionary,String> implements DataDictionaryDao{
	
	
	public DataDictionaryDaoImpl() {
		super(DataDictionary.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public DataDictionary addNewDictionary(DataDictionary dictionary)
			throws EazyBpmException {
		if (log.isDebugEnabled()) {
			//log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(dictionary);
		getSession().flush();
		return dictionary;
	}
	
	/**
	 * hibernate主键生成策略会覆盖导入对象中的id值，导致无法用session自带的方法保存
	 * 使用原生sql插入
	 */
	public DataDictionary importDictionary(DataDictionary dictionary)
			throws EazyBpmException {
		
		String id = dictionary.getId();
		String name = dictionary.getName();
		String description = dictionary.getDescription();
		String type = dictionary.getType();
		String value = dictionary.getValue();
		String sqlString = dictionary.getSqlString();
		Integer isEnabled = dictionary.getIsEnabled()? 1:0;
		String code = dictionary.getCode();
		String createdBy = dictionary.getCreatedBy();
		String parentDictId = dictionary.getParentDictId();
		Integer orderNo = dictionary.getOrderNo();
		
		Query query = getSession().createSQLQuery(
				"insert into ETEC_DATA_DICTIONARY(ID, NAME, DESCRIPTION, DICTIONARY_TYPE, DICTIONARY_VALUE, SQLSTRING, IS_ENABLED, CODE, CREATED_BY, PARENT_DICT_ID, ORDER_NO) values ("
				+ "'"+id+"', '"+name+"','"+description+"','"+type+"','"+value+"'," + "'"+ sqlString + "'," + isEnabled+",'"+code+"'," + "'"+createdBy+"','"+parentDictId+"'," + orderNo + ")");
       	query.executeUpdate();
       	
		return dictionary;
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteDictionary() throws EazyBpmException {

	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<DataDictionary> getAllDataDictionaries() throws EazyBpmException {
		
		Query qry = getSession().createQuery(
				"from DataDictionary dataDictionary ");
		return qry.list();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public DataDictionary getDataDictionaryById(String id) throws EazyBpmException {

		Query qry = getSession().createQuery(
				"from DataDictionary dataDictionary where dataDictionary.id = '"
						+ id + "'");
		List<DataDictionary> dictionaries = qry.list();

		return (dictionaries != null && !dictionaries.isEmpty()) ? dictionaries
				.get(0) : new DataDictionary();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getDictionaryLabelValueByParentId(String parentId)
			throws EazyBpmException {

		Query qry = getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(dataDictionary.name as name,dataDictionary.id as id) from DataDictionary dataDictionary where dataDictionary.parentDictId = '"
								+ parentId + "'");
		List<LabelValue> dictionaries = qry.list();

		return dictionaries;

	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getDictionaryValueByParentId(String parentId)
			throws EazyBpmException {

		Query qry = getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(dataDictionary.value as value,dataDictionary.id as id) from DataDictionary dataDictionary where dataDictionary.parentDictId = '"
								+ parentId + "' and dataDictionary.isEnabled =true");
		List<LabelValue> dictionaries = qry.list();

		return dictionaries;

	}
	/**
	 *{@inheritDoc} 
	 */
	public List<DataDictionary> getDataDictionaryByParentId(String parentId) throws EazyBpmException {

		Query qry = getSession()
				.createQuery(
						"from DataDictionary dataDictionary where dataDictionary.parentDictId = '"
								+ parentId + "' order by dataDictionary.orderNo asc");
		List<DataDictionary> dictionaries = qry.list();

		return dictionaries;
	}
	
	public List<DataDictionary> getDataDictionaryByDictionaryIds(String dictionaryIds) throws EazyBpmException {

		Query qry = getSession()
				.createQuery(
						"from DataDictionary dataDictionary where dataDictionary.id in ("
								+ dictionaryIds + ") order by dataDictionary.orderNo asc");
		List<DataDictionary> dictionaries = qry.list();

		return dictionaries;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllDictionaryAsLabelValue()
			throws EazyBpmException {

		Query qry = getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(dataDictionary.name as name,dataDictionary.id as id) from DataDictionary dataDictionary where (dataDictionary.parentDictId = 'UserDefined' or dataDictionary.parentDictId = 'SystemDefined') and dataDictionary.isEnabled =true");
		List<LabelValue> dictionaries = qry.list();

		return dictionaries;

	}
	/**
	 * {@inheritDoc}
	 */
	public boolean checkDictionaryNameExists(String dictName) throws EazyBpmException {
		
		Query qry = getSession()
				.createQuery(
						"select dataDictionary from DataDictionary dataDictionary where lower(dataDictionary.name) = '"+dictName+"' ");
		List<DataDictionary> dictionaries = qry.list();

		return (dictionaries != null && !dictionaries.isEmpty()) ? true:false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean checkDictionaryValueExists(String dictValue) throws EazyBpmException {
		
		Query qry = getSession()
				.createQuery(
						"select dataDictionary from DataDictionary dataDictionary where lower(dataDictionary.value) = '"+dictValue+"' ");
		List<DataDictionary> dictionaries = qry.list();

		return (dictionaries != null && !dictionaries.isEmpty()) ? true:false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean checkDictionaryCodeExists(String dictCode) throws EazyBpmException {
		
		Query qry = getSession()
				.createQuery(
						"select dataDictionary from DataDictionary dataDictionary where lower(dataDictionary.code) = '"+dictCode+"' ");
		List<DataDictionary> dictionaries = qry.list();

		return (dictionaries != null && !dictionaries.isEmpty()) ? true:false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteDataDictionary(String idList) throws EazyBpmException {

		Query qry = getSession().createQuery(
				"delete from DataDictionary dataDictionary where dataDictionary.id in ("
						+ idList + ")  or dataDictionary.parentDictId in ("+ idList+") ");
		int result = qry.executeUpdate();
		

		return result != 0  ? true : false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean disableDataDictionary(String idList) throws EazyBpmException {

	
		Query qry = getSession()
				.createQuery(
						"update DataDictionary dataDictionary set dataDictionary.isEnabled = false where dataDictionary.id in ("
								+ idList + ") ");
		int result = qry.executeUpdate();
		

		return result != 0  ? true : false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateDictionaryOrderNos(String dictionaryId,int orderNo) throws EazyBpmException {
		
		Query qry = getSession()
				.createQuery(
						"update DataDictionary dataDictionary set dataDictionary.orderNo = "+orderNo+" where dataDictionary.id = '"
								+ dictionaryId + "' ");
		int result = qry.executeUpdate();
		
		return result != 0  ? true : false;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<?> getDynamicDictionaryValues(String query) throws EazyBpmException {
		
		List<?> list = null;
			Query qry = getSession()
					.createSQLQuery(query);
	    list = qry.list();
		return list;
		
	}
	
	public DataDictionary getDataDictionaryByCode(String code) {

		Query qry = getSession().createQuery("from DataDictionary dataDictionary where dataDictionary.code = '" + code + "'");
		List<DataDictionary> dictionaries = qry.list();

		return (dictionaries != null && !dictionaries.isEmpty()) ? dictionaries.get(0) : new DataDictionary();
	}
	
	
	/**
	 * Description:违规条例字典集
	 * 作者 : 蒋晨   
	 * 时间 : 2017-11-11 下午1:40:10
	 */
	@Override
	public List<DataDictionary> getWgtlList(String wgtl) {
		/*Query qry = getSession().createQuery(
				"from DataDictionary dataDictionary where dataDictionary.IS_ENABLED = 1 and  dataDictionary.PARENT_DICT_ID in (select b.id from DataDictionary b where b.PARENT_DICT_ID = 'UserDefined' and b.IS_ENABLED=1 and b.code ='"+wgtl+"'");
		return qry.list();*/
		String sql = "select a.code,a.name  from etec_data_dictionary a where a.IS_ENABLED = 1 and  a.PARENT_DICT_ID in (select b.id from etec_data_dictionary b where b.PARENT_DICT_ID = 'UserDefined' and b.IS_ENABLED=1 and b.code = ?)";
		//System.out.println(sql);
		List<DataDictionary> list = getSession().createSQLQuery(sql)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("name", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(DataDictionary.class)).setParameter(0, wgtl).list();
		return list;
		
	}
	
	
	/**
	 * Description:违规条例字典集
	 * 作者 : 蒋晨   
	 * 时间 : 2017-11-11 下午1:40:10
	 */
	@Override
	public List<DataDictionary> getWgtlAll() {
		/*Query qry = getSession().createQuery(
				"from DataDictionary dataDictionary where dataDictionary.IS_ENABLED = 1 and  dataDictionary.PARENT_DICT_ID in (select b.id from DataDictionary b where b.PARENT_DICT_ID = 'UserDefined' and b.IS_ENABLED=1 and b.code ='"+wgtl+"'");
		return qry.list();*/
		String sql = "select b.code,b.name from etec_data_dictionary b where b.PARENT_DICT_ID = 'UserDefined' and b.IS_ENABLED=1";
		//System.out.println(sql);
		List<DataDictionary> list = getSession().createSQLQuery(sql)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("name", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(DataDictionary.class)).list();
		return list;
		
	}
}
