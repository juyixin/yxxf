package com.eazytec.bpm.oa.mail.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.oa.mail.dao.MailDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.EmailConfiguration;
import com.eazytec.model.EmailContact;

/**
 * This class interacts with Hibernate session to save/delete,
 * retrieve Email Contact objects.
 *
 * author mathi
*/
@Repository("mailDao")
public class MailDaoImpl extends GenericDaoHibernate<EmailContact, String> implements MailDao{

	/**
     * Constructor that sets the entity to EmailContact.class.
     */
    public MailDaoImpl() {
        super(EmailContact.class);
    }
    
    /**
	 * {@inheritDoc}
	 */
	public List<EmailContact> getUserContacts(String user) {
		List<EmailContact> contacts = getSession().createCriteria(EmailContact.class)
				.add(Restrictions.eq("createdBy", user)).list();
		return contacts;
	}

	 /**
     * {@inheritDoc}
     */
    public List<EmailContact> getContactsByIds(List<String> ids) {
    	List<EmailContact> contacts = getSession().createQuery("from EmailContact as contact where contact.id in (:list)").setParameterList("list", ids).list();
        if (contacts.isEmpty()) {
            return null;
        } else {
            return contacts;
        }
    }
    
    
	/**
	 * {@inheritDoc}
	 */
	public EmailConfiguration saveEmailConfig(EmailConfiguration emailConfig) {
        getSession().saveOrUpdate(emailConfig);
        getSession().flush();
		return emailConfig;
	}
	
	 /**
	 * {@inheritDoc}
	 */
	public EmailConfiguration getEmailConfig() {
		List<EmailConfiguration> emailConfig = getSession().createCriteria(EmailConfiguration.class).list();
		if(emailConfig.size() > 0){
			return emailConfig.get(0);
		} else {
			return null;
		}
	}
}
