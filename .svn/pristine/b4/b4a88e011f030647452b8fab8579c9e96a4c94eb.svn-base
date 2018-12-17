package com.eazytec.exceptions;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.Authentication;

public class BPMAccountStatusException extends AccountStatusException {

	 private Authentication authentication;
	 
	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public BPMAccountStatusException(String msg) {
		super(msg);
	}

    @Deprecated
    public BPMAccountStatusException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified
     * message and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public BPMAccountStatusException(String msg, Throwable t) {
        super(msg, t);
    }
}
