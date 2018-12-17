package com.eazytec.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eazytec.dao.UserDao;
import com.eazytec.model.User;

public class LdapAuthoritiesUserDetailsServiceImpl implements UserDetailsService {

    private final transient Log log = LogFactory.getLog(getClass());
    private UserDao userDao;
	
    @Autowired    
	public LdapAuthoritiesUserDetailsServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
    
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		User user = (User) userDao.loadUserByUsername(username);
		return user;
	}
}