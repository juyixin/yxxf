package com.eazytec.service;


/**
 * An exception that is thrown by classes wanting to trap unique 
 * constraint violations.  This is used to wrap Spring's 
 * DataIntegrityViolationException so it's checked in the web layer.
 *
 * @author sri sudha
 */
public class TriggerExistsException extends Exception {
    private static final long serialVersionUID = 4050482305178810163L;

    /**
     * Constructor for TriggerExistsException.
     *
     * @param message exception message
     */
    public TriggerExistsException(final String message) {
        super(message);
    }
}
