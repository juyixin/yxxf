/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.io.*;
import java.util.*;

import dtw.webmail.JwmaKernel;

/**
 * Class implementing the JwmaError model.
 * It can contain an embedded exception and allows
 * access to it and it's corresponding stack trace.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaException
    extends Exception
    implements JwmaError {

  //instance attributes
  private boolean inline;
  private boolean displayed = false;
  private Set m_Descriptions = new HashSet(5);
  private Exception m_Exception;

  /**
   * Constructs a new <tt>JwmaException</tt> instance
   * with a given description.
   *
   * @param msg the description of the Exception as <tt>String</tt>.
   */
  public JwmaException(String msg) {
    super(msg);
    addDescription(msg);
    inline = false;
  }//constructor

  /**
   * Constructs a new <tt>JwmaException</tt> instance.
   * with a given description and possibly as inline error.
   *
   * @param msg the description of the Exception as <tt>String</tt>.
   * @param inlined true when inline error, false otherwise.
   */
  public JwmaException(String msg, boolean inlined) {
    super(msg);
    addDescription(msg);
    inline = inlined;
  }//constructor

  /**
   * Sets the flag that controls if the Exception represents
   * an inline error.
   * <p>
   * An inline error should be displayed within the same view
   * and not on the error view.
   *
   * @param true if the overriding is to be allowed, false otherwise.
   * @return reference to this exception.
   */
  public JwmaException setInlineError(boolean b) {
    inline = b;
    return this;
  }//setInlineError

  public boolean isInlineError() {
    return inline;
  }//isInlineError

  public void addDescription(String description) {
    m_Descriptions.add(description);
  }//addDescription

  public String[] getDescriptions() {
    String[] desc = new String[m_Descriptions.size()];
    return (String[]) m_Descriptions.toArray(desc);
  }//getDescriptions

  public Iterator iterator() {
    return m_Descriptions.iterator();
  }//iterator

  /**
   * Resolves the description for the error in the
   * given language.
   *
   * @param language the string denoting the language.
   *
   public void resolveDescription(String language) {
   if(!isResolved()) {
   m_Description= JwmaKernel.getReference()
   .getErrorMessage(super.getMessage(),language);
   setResolved(true);
   }
   }//resolveDescription

   /**
   * Tests if the error has been resolved to
   * an i18n description.
   *
   * @return true if it was resolved, false otherwise.
   *
   public boolean isResolved() {
   return m_Resolved;
   }//isResolved

   /**
   * Sets the flag that controls if this error has already
   * i18n resolved the description.
   *
   * @param true if resolved, false otherwise.
   * @return reference to this exception.
   *
   public JwmaException setResolved(boolean b) {
   m_Resolved=true;
   return this;
   }//setResolved
   */

  /**
   * Sets the flag that controls if this error was already
   * displayed.
   *
   * @param true if the overriding is to be allowed, false otherwise.
   */
  public void setDisplayed(boolean b) {
    displayed = b;
  }//setDisplayed

  public boolean isDisplayed() {
    return displayed;
  }//isDisplayed

  /**
   * Sets an embedded exception.
   *
   * @param ex the exception to be embedded.
   * @return reference to this exception.
   */
  public JwmaException setException(Exception ex) {
    m_Exception = ex;
    return this;
  }//setException

  public boolean hasException() {
    return (m_Exception != null);
  }//hasException

  public String getExceptionTrace() {
    if (hasException()) {
      StringWriter trace = new StringWriter();
      m_Exception.printStackTrace(new PrintWriter(trace));
      return trace.toString();
    } else {
      return "";
    }
  }//getExceptionTrace

  /**
   * Returns the embedded exception.
   *
   * @return the embedded exception.
   */
  public Exception getException() {
    return m_Exception;
  }//getException

}//JwmaException
