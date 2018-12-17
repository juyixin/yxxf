package com.eazytec.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.eazytec.dao.GenericDao;
import com.eazytec.util.BeanUtils;

/**
 * This class is the Base class for all DAOs holding
 * common CRUD methods that they might all use. Can be extended
 * when we require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="com.eazytec.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="com.eazytec.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author madan
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoHibernate<T, PK extends Serializable> implements GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;
    @Resource
    private SessionFactory sessionFactory;

    /**
     * Constructor that takes in a class to see which type of entity to persist, used in subclassing
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * Constructor that takes in a class and sessionFactory for easy creation of DAO.
     *
     * @param persistentClass the class type you'd like to persist
     * @param sessionFactory  the pre-configured Hibernate SessionFactory
     */
    public GenericDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public Session getSession() throws HibernateException {
        Session sess = getSessionFactory().getCurrentSession();
        if (sess == null) {
            sess = getSessionFactory().openSession();
        }
        return sess;
    }

    @Autowired
    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        Session sess = getSession();
        return sess.createCriteria(persistentClass).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection<T> result = new LinkedHashSet<T>(getAll());
        return new ArrayList<T>(result);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        
        /**
        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }
        **/

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T save(T object) {
        Session sess = getSession();
        return (T) sess.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        Session sess = getSession();
        sess.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        sess.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Session sess = getSession();
        Query namedQuery = sess.getNamedQuery(queryName);

        for (String s : queryParams.keySet()) {
            namedQuery.setParameter(s, queryParams.get(s));
        }

        return namedQuery.list();
    }
    
    
    
    /**
	 * 通过Updater更新对象
	 */
	@SuppressWarnings("unchecked")
	public T updateByUpdater(Updater<T> updater) {
		ClassMetadata cm = sessionFactory.getClassMetadata(persistentClass);
		T bean = updater.getBean();
		T po = (T) getSession().get(persistentClass, cm.getIdentifier(bean, (SessionImplementor)getSession()));
		updaterCopyToPersistentObject(updater, po, cm);
		return po;
	}
    
	/**
	 * 将更新对象拷贝至实体对象，并处理many-to-one的更新。
	 * 
	 * @param updater
	 * @param po
	 */
	private void updaterCopyToPersistentObject(Updater<T> updater, T po, ClassMetadata cm) {
		String[] propNames = cm.getPropertyNames();
		String identifierName = cm.getIdentifierPropertyName();
		T bean = updater.getBean();
		Object value;
		for (String propName : propNames) {
			if (propName.equals(identifierName)) {
				continue;
			}
			try {
				value = BeanUtils.getSimpleProperty(bean, propName);
				if (!updater.isUpdate(propName, value)) {
					continue;
				}
				cm.setPropertyValue(po, propName, value);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("copy property to persistent object failed: '" + propName + "'", e);
			}
		}
	}
    
    /**
	 * 通过HQL查询对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}
    
	/**
	 * 通过Finder获得列表
	 */
	public List find(Finder finder) {
		Query query = finder.createQuery(getSession());
		return query.list();
	}
    
	/**
	 * 按属性查找对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByProperty(String property, Object value) {
		return createCriteria(Restrictions.eq(property, value)).list();
	}
    
	public int findCount(String hql, Object... values) {
		Query query = createQuery(hql, values);
		Object obj = query.iterate().next();
		return obj == null ? 0 : ((Number) obj).intValue();
	}
	
	public int findCount(Finder finder) {
		Query query = finder.createQuery(getSession());
		return ((Number) query.iterate().next()).intValue();
	}
	
	/**
	 * 按属性统计记录数
	 */
	public int findCountByProperty(String property, Object value) {
		return ((Number) (createCriteria(Restrictions.eq(property, value)).setProjection(Projections.rowCount()).uniqueResult())).intValue();
	}
	
	/**
	 * 通过HQL查询唯一对象
	 */
	@SuppressWarnings("unchecked")
	public T findUnique(String hql, Object... values) {
		return (T) createQuery(hql, values).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public T findUnique(Finder f) {
		return (T) f.createQuery(getSession()).uniqueResult();
	}
	
	/**
	 * 按属性查找唯一对象
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByProperty(String property, Object value) {
		return (T) createCriteria(Restrictions.eq(property, value)).setCacheable(true).uniqueResult();
	}
	
	public int executeUpdate(String hql, Object... values) {
		Query query = createQuery(hql, values);
		return query.executeUpdate();
	}

	public int executeUpdate(Finder finder) {
		Query query = finder.createQuery(getSession());
		return query.executeUpdate();
	}
	
	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString, Object... values) {
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}
	
	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(persistentClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
    
}
