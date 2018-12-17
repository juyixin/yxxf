/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

/**
 * An interface defining the contract for interaction with
 * the JwmaError model.
 * <p>
 * The JwmaError allows a view programmer to obtain
 * information about an error to display it in free style
 * and in any language. Note that the description will be in
 * the language of the <tt>errormessages.properties</tt> file.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 *
 */
public interface JwmaError {

  /**
   * Tests if the error is an inline error.
   * <p>
   * An inline error should be displayed within the same view
   * and not on the error view.
   *
   * @return true if it is an inline error, false otherwise.
   */
  public boolean isInlineError();

  /**
   * Tests if the error has been displayed.
   *
   * @return true if it was displayed, false otherwise.
   */
  public boolean isDisplayed();

  /**
   * Set's the flag that stores if an error has
   * been displayed.
   *
   * @param b true if it was displayed, false otherwise.
   */
  public void setDisplayed(boolean b);


  /**
   * Returns an <tt>String[]</tt> containing the
   * keys to the locale specific error descriptions.
   *
   * @return an array of strings containing the error
   *         description keys.
   */
  public String[] getDescriptions();

  /**
   * Tests if the error has an embedded exception.
   *
   * @return true if error has an embedded exception,
   *         false otherwise.
   */
  public boolean hasException();

  /**
   * Returns a <tt>String</tt> representing the Errors
   * embedded exception stack trace.
   *
   * @return the error's embedded exception stacktrace as
   *         <tt>String</tt>.
   */
  public String getExceptionTrace();

}//JwmaError
