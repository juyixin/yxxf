package com.eazytec.link.returns;

import java.util.List;

public class LinkResult {
	
	private List<LinkInfo> resultLink;
	
	private String code;
	
	private String message;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<LinkInfo> getResultLink() {
		return resultLink;
	}

	public void setResultLink(List<LinkInfo> resultLink) {
		this.resultLink = resultLink;
	}
	
	

}
