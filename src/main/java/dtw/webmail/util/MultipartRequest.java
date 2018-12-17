/***
package dtw.webmail.util;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import dtw.webmail.model.JwmaMessagePartImpl;
import dtw.webmail.JwmaKernel;

/**
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

  /**
  public MultipartRequest(ServletRequest request, int limit)
      throws IOException {

    //Create with size from Kernel which comes in kb
    //    .getSettings().getMailTransportLimit() * 1024;
    m_Limit = limit * 1024;

    m_Request = request;
    processRequest();
  }//constructor


  /**
  public Enumeration getParameterNames() {
    return m_Parameters.keys();
  }//getParameterNames

  /**
  public String getParameter(String name) {
    if (m_Parameters.containsKey(name)) {
      return ((String[]) m_Parameters.get(name))[0];
    } else {
      return null;
    }
  }//getParameter

  /**
  public String[] getParameterValues(String name) {
    if (m_Parameters.containsKey(name)) {
      return (String[]) m_Parameters.get(name);
    } else {
      return null;
    }
  }//getParameterValues

  /**
  public boolean hasAttachments() {
    try {
      return (m_FormdataMultipart.getCount() > 0);
    } catch (MessagingException ex) {
      return false;
    }
  }//hasAttachments

  /**
  public MimeMultipart getAttachments() {
    return (MimeMultipart) m_FormdataMultipart;
  }//getAttachments

  /**
  public void processRequest()
      throws IOException {

    //JwmaKernel.getReference().debugLog().write("Processing request data..");
      throw new IOException("Posted data exceeds limit of " +
          m_Limit + " bytes.");
    }
      throw new IOException("Can only handle an incoming multipart/form-data stream:" + ctype);
    }*/
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
    ctype = sbuf.toString();
        m_Request.getInputStream(), ctype, m_Limit
    );


    try {
      m_FormdataMultipart = new FormdataMultipart(m_InputStream);
      m_Parameters = m_FormdataMultipart.getParameters();

    } catch (MessagingException mex) {

      //JwmaKernel.getReference().debugLog().writeStackTrace(mex);
      throw new IOException(mex.getMessage());
    }
  }//processRequest
  private static final int FORMDATA_LIMIT = 1024 * 1024;  // 1 Meg

}//class MultipartRequest