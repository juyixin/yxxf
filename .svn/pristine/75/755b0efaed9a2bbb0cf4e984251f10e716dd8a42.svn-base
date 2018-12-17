/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

//import dtw.webmail.JwmaKernel;

/**
 * Class that implements a Multipart that handles
 * the <tt>multipart/form-data</tt> content type.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class FormdataMultipart extends MimeMultipart {

  //logging
  private static Logger log = Logger.getLogger(FormdataMultipart.class);

  //instance attributes
  private Hashtable m_Params = new Hashtable();
  private boolean m_Removed = false;

  /**
   * Constructs a <tt>FormdataMultipart</tt> instance.<br>
   * This implementation just calls the superclass constructor.
   *
   * @return the newly created <tt>FormdataMultipart</tt> instance.
   */
  public FormdataMultipart() {
    super();
  }//constructor

  /**
   * Constructs a <tt>FormdataMultipart</tt> instance.<br>
   * Automatically processes the body parts to extract parameters,
   * and attachments.
   *
   * @param DataSource to construct the Multipart from, will be a
   *        <tt>MultipartInputStream</tt>.
   *
   * @return the newly created <tt>FormdataMultipart</tt> instance.
   */
  public FormdataMultipart(DataSource ds)
      throws MessagingException, IOException {

    super(ds);
    //processBodyParts();
    updateHeaders();
  }//constructor

  /**
   * Returns the extracted parameters (with the extrcted values)
   * as <tt>Hashtable</tt>.
   *
   * @return the extracted parameter data as <tt>Hashtable</tt>
   */
  public Hashtable getParameters() {
    return m_Params;
  }//getParameters

  /**
   * Processes the body parts of the form-data.
   * Extracts parameters and set values, and
   * leaves over the attachments.
   *
   * @throws IOException if i/o operations fail.
   * @throws MessagingException if parsing or part handling with
   *         Mail API classes fails.
   */
  private void processBodyParts()
      throws IOException, MessagingException {

    //if write out to log for debug reasons!
    //ByteArrayOutputStream bout=new ByteArrayOutputStream();
    //writeTo(bout);
    //JwmaKernel.getReference().debugLog().write(bout.toString());

    for (int i = 0; i < getCount(); i++) {
      MimeBodyPart mbp = (MimeBodyPart) getBodyPart(i);
      processBodyPart(mbp);
      if (m_Removed) {
        m_Removed = false;
        //decrease index i approbiately
        i--;
      }
    }


    setSubType("mixed");
    //JwmaKernel.getReference().debugLog().write(
    //	"Processed multipart/form-data. Attachment parts:"+getCount()
    //);
  }//processParts

  /**
   * Processes a body part of the form-data.
   * Extracts parameters and set values, and
   * leaves over the attachments.
   *
   * @param mbp the <tt>MimeBodyPart</tt> to be processed.
   *
   * @throws IOException if i/o operations fail.
   * @throws MessagingException if parsing or part handling with
   *         Mail API classes fails.
   */
  private void processBodyPart(MimeBodyPart mbp)
      throws MessagingException, IOException {

    //String contenttype=new String(mbp.getContentType());
    //JwmaKernel.getReference().debugLog().write("Processing "+contenttype);

    //check if a content-type is given
    String[] cts = mbp.getHeader("Content-Type");
    if (cts == null || cts.length == 0) {
      //this is a parameter, get it out and
      //remove the part.
      String controlname = extractName(
          (mbp.getHeader("Content-Disposition"))[0]);

      //JwmaKernel.getReference().debugLog().write("Processing control:"+controlname);
      //retrieve value observing encoding
      InputStream in = mbp.getInputStream();
      String[] encoding = mbp.getHeader("Content-Transfer-Encoding");
      if (encoding != null && encoding.length > 0) {
        in = MimeUtility.decode(in, encoding[0]);
      }

      String value = extractValue(in);
      if (value != null || !value.trim().equals("")) {
        addParameter(controlname, value);
      }
      //flag removal
      m_Removed = true;
      removeBodyPart(mbp);
    } else {
      String filename = extractFileName(
          (mbp.getHeader("Content-Disposition"))[0]);

      //normally without file the control should be not successful.
      //but neither netscape nor mircosoft iexploder care much.
      //the only feature is an empty filename.
      if (filename.equals("")) {
        //kick it out too
        m_Removed = true;
        removeBodyPart(mbp);
      } else {

        //JwmaKernel.getReference().debugLog().write("Incoming filename="+filename);

        //IExploder sends files with complete path.
        //jwma doesnt want this.
        int lastindex = filename.lastIndexOf("\\");
        if (lastindex != -1) {
          filename = filename.substring(lastindex + 1, filename.length());
        }

        //JwmaKernel.getReference().debugLog().write("Outgoing filename="+filename);

        //Observe a possible encoding
        InputStream in = mbp.getInputStream();
        String[] encoding = mbp.getHeader("Content-Transfer-Encoding");
        if (encoding != null && encoding.length > 0) {
          in = MimeUtility.decode(in, encoding[0]);
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStream out = (OutputStream) bout;

        int i = 0;
        while ((i = in.read()) != -1) {
          //maybe more efficient in buffers, but well
          out.write(i);
        }
        out.flush();
        out.close();

        //create the datasource
        MimeBodyPartDataSource mbpds =
            new MimeBodyPartDataSource(
                //		contenttype,filename,bout.toByteArray()
                cts[0], filename, bout.toByteArray()
            );


        //Re-set the Content-Disposition header, in case
        //the file name was changed
        mbp.removeHeader("Content-Disposition");
        mbp.addHeader(
            "Content-Disposition", "attachment; filename=\"" +
            filename +
            "\""
        );

        //set a base64 transferencoding und the data handler
        mbp.addHeader("Content-Transfer-Encoding", "base64");
        mbp.setDataHandler(new DataHandler(mbpds));
      }
    }
  }//processBodyPart

  /**
   * Returns the name of a parameter by extracting it
   * from the content-disposition header line.
   *
   * @param disposition the content-disposition header line as
   *        <tt>String</tt>.
   *
   * @return the name of the parameter as <tt>String</tt>.
   *
   * @throws IOException if the header line is malformed.
   */
  private String extractName(String disposition)
      throws IOException {

    int end = 0;
    int start = -1;

    start = disposition.indexOf("name=\"");
    end = disposition.indexOf("\"", start + 7);   //offset is to skip name=\"
    if (start == -1 || end == -1) {
      throw new IOException("Mime header malformed.");
    }
    return disposition.substring(start + 6, end);
  }//extractName

  /**
   * Returns the filename of an attachment by extracting it
   * from the content-disposition header line.
   *
   * @param disposition the content-disposition header line as
   *        <tt>String</tt>.
   *
   * @return the filename of the attachment as <tt>String</tt>.
   *
   * @throws IOException if the header line is malformed.
   */
  private String extractFileName(String disposition)
      throws IOException {

    int end = 0;
    int start = -1;

    start = disposition.indexOf("filename=\"");
    end = disposition.indexOf("\"", start + 10);   //offset is to skip filename=\"
    if (start == -1 || end == -1) {
      throw new IOException("Mime header malformed.");
    }
    return disposition.substring(start + 10, end);
  }//extractFileName

  /**
   * Returns the value of a parameter by extracting it
   * from the <tt>InputStream</tt> that represents the content
   * of the (parameter) part.
   *
   * @param in <tt>InputStream</tt> that reads from the content
   *        of the (parameter) part.
   *
   * @return the value of the parameter as <tt>String</tt>.
   *
   * @throws IOException if reading from the stream fails.
   */
  private String extractValue(InputStream in)
      throws IOException {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int i = 0;
    while ((i = in.read()) != -1) {
      out.write(i);
    }
    out.flush();
    out.close();
    in.close();

    //JwmaKernel.getReference().debugLog().write("Retrieved value="+out.toString());
    //apply a little bit of magic when returning
    return out.toString("iso-8859-1");
  }//extractValue

  /**
   * Adds a parameter and mapped value to the parameters collection.
   * If the parameter already exists, it adds another value to
   * an already existing parameter by extending the array of strings.
   *
   * @param name the name of the parameter as <tt>String</tt>.
   * @param value the value of the parameter as <tt>String</tt>.
   */
  private void addParameter(String name, String value) {
    String values[];

    //JwmaKernel.getReference().debugLog().write("Adding "+name+"="+value);

    if (m_Params.containsKey(name)) {
      String oldValues[] = (String[]) m_Params.get(name);
      values = new String[oldValues.length + 1];
      for (int i = 0; i < oldValues.length; i++) {
        values[i] = oldValues[i];
      }
      values[oldValues.length] = value;
    } else {
      values = new String[1];
      values[0] = value;
    }
    m_Params.put(name, values);
  }//addParameter

}//FormdataMultipart