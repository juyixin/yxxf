package com.eazytec.quartz;


import java.util.Enumeration;

import javax.jws.WebService;
import javax.persistence.Id;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServerConfig {
	
	private String serverRootUrl;
    
    public String getServerRootUrl(){ 
    	return serverRootUrl;
    }
    
    public void setserverRootUrl(String rootUrl){
        this.serverRootUrl=rootUrl;
    }
    
}