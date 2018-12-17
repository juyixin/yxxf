/**** jwma Java WebMail* Copyright (c) 2000-2003 jwma team** jwma is free software; you can distribute and use this source* under the terms of the BSD-style license received along with* the distribution. ***/
package dtw.webmail.util;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import dtw.webmail.model.JwmaMessagePartImpl;
import dtw.webmail.JwmaKernel;

/** * Class that encapsulates a MultipartRequest, internally * handling it. * <p> * The protocol to access the parameters resembles the one * of the <tt>ServletRequest</tt> class. * * @author Dieter Wimberger * @version 0.9.7 07/02/2003 */
public class MultipartRequest {

  //logging
  private static Logger log = Logger.getLogger(MultipartRequest.class);

  //instance attributes
  private int m_Limit;

  private ServletRequest m_Request;
  private Hashtable m_Parameters;
  private FormdataMultipart m_FormdataMultipart;

  /**
   * Constructs a <tt>MultipartRequest</tt> instance, with an
   * upload limit of 2048 kB.
   *
   * @return the newly constructed <tt>MultipartRequest</tt> instance.
   */
  public MultipartRequest(ServletRequest request)
      throws IOException {

    m_Limit = 2048 * 1024;

    m_Request = request;
    processRequest();
  }//constructor

  /**   * Constructs a <tt>MultipartRequest</tt> instance.   *   * @return the newly constructed <tt>MultipartRequest</tt> instance.   */
  public MultipartRequest(ServletRequest request, int limit)
      throws IOException {

    //Create with size from Kernel which comes in kb    //m_Limit = JwmaKernel.getReference()
    //    .getSettings().getMailTransportLimit() * 1024;
    m_Limit = limit * 1024;

    m_Request = request;
    processRequest();
  }//constructor


  /**   * Returns the names of all the parameters as an Enumeration of   * strings.<br>   * It returns an empty Enumeration if there are no parameters.   *   * @return Enumeration of the names of all the parameters as strings.   */
  public Enumeration getParameterNames() {
    return m_Parameters.keys();
  }//getParameterNames

  /**   * Returns the value of a given parameter as String, or null if   * the control was not successful or non existant.   * <i><b>Note:</b><br>   * Mimics servlet request, do not call for multi value   * parameters.</i>   *   * @param name of the parameter to be retrieved as <tt>String</tt>.   *   * @return the parameter's value as <tt>String</tt>.   */
  public String getParameter(String name) {
    if (m_Parameters.containsKey(name)) {
      return ((String[]) m_Parameters.get(name))[0];
    } else {
      return null;
    }
  }//getParameter

  /**   * Returns all values of a given parameter as an array of strings.   *   * If this MultipartRequest does not contain any values for this   * parameter name, then this method returns null.   * Otherwise the array contains one <tt>String</tt> for each value   * of this parameter.   *   * @param name of the parameter to be retrieved as <tt>String</tt>.   *   * @return an array of strings,each representing a value of the   *         parameter.   */
  public String[] getParameterValues(String name) {
    if (m_Parameters.containsKey(name)) {
      return (String[]) m_Parameters.get(name);
    } else {
      return null;
    }
  }//getParameterValues

  /**   * Tests if this <tt>MultipartRequest</tt> has attachments.   *   * @return true if it has attachments, false otherwise.   */
  public boolean hasAttachments() {
    try {
      return (m_FormdataMultipart.getCount() > 0);
    } catch (MessagingException ex) {
      return false;
    }
  }//hasAttachments

  /**   * Returns the attachments as <tt>MimeMultipart</tt>.   *   * @return the attachments contained within in a <tt>MimeMultipart</tt>   *         instance.   */
  public MimeMultipart getAttachments() {
    return (MimeMultipart) m_FormdataMultipart;
  }//getAttachments

  /**   * Parses the incoming multipart/form-data by reading it from the   * given request's input stream.   *   * <i><b>Note:</b>Does not handle multiparts contained in multiparts.</i>   *   * @throws IOException if uploaded content larger than allowed   *         or parsing failed.   */
  public void processRequest()
      throws IOException {

    //JwmaKernel.getReference().debugLog().write("Processing request data..");    //first check on content length    int length = m_Request.getContentLength();    //JwmaKernel.getReference().debugLog().write("Got Length..");    if (length > m_Limit) {
      throw new IOException("Posted data exceeds limit of " +
          m_Limit + " bytes.");
    }    //then check for the content type and contained boundary    String ctype = m_Request.getContentType();    //JwmaKernel.getReference().debugLog().write("Got content-type ("+ctype+")");    /*if (ctype.indexOf("multipart/form-data") == -1) {
      throw new IOException("Can only handle an incoming multipart/form-data stream:" + ctype);
    }*/    //Quote unquoted boundaries    //Opera for example sends unquoted boundaries with non-safe characters    StringBuffer sbuf = new StringBuffer();
    int idx = ctype.indexOf("boundary=");
    sbuf.append(ctype.substring(0, idx));
    sbuf.append("boundary=");
    String boundary = ctype.substring(idx + 9, ctype.length());
    if (boundary.charAt(0) == '"') {
      sbuf.append(boundary);
    } else {
      sbuf.append("\"");
      sbuf.append(boundary);
      sbuf.append("\"");
    }
    ctype = sbuf.toString();    //JwmaKernel.getReference().debugLog().write("Streaming in...");    MultipartInputStream m_InputStream = new MultipartInputStream(
        m_Request.getInputStream(), ctype, m_Limit
    );


    try {
      m_FormdataMultipart = new FormdataMultipart(m_InputStream);
      m_Parameters = m_FormdataMultipart.getParameters();

    } catch (MessagingException mex) {

      //JwmaKernel.getReference().debugLog().writeStackTrace(mex);      log.error("Failed to process multipart/form-data request.", mex);
      throw new IOException(mex.getMessage());
    }
  }//processRequest  //constant definitions  /**   * Defines the size limit of the upload.   */
  private static final int FORMDATA_LIMIT = 1024 * 1024;  // 1 Meg

}//class MultipartRequest