/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util;

import java.io.*;
import javax.activation.DataSource;

import org.apache.log4j.Logger;

/**
 * Class that provides a MultipartInputStream by wrapping
 * an existant InputStream.<br>
 * It implements size limit checking and serves as a
 * <tt>DataSource</tt> for handling with
 * Mail API (or other JAF aware) classes.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class MultipartInputStream
    extends FilterInputStream
    implements DataSource {

  //logging
  private static Logger log =
      Logger.getLogger(MultipartInputStream.class);


  //instance attributes
  private int m_Limit;
  private String m_ContentType;
  private int m_BytesRead = 0;

  /**
   * Constructs a <tt>MultipartInputStream</tt> instance.
   *
   * @param in the InputStream this instance reads from.
   * @param ctype the content type of the incoming request as <tt>
   *        String</tt>. Important because it contains the parts border!
   * @param readlimit the maximum size of the complete request data as
   *        <tt>int</tt>.
   *
   * @return the newly created <tt>MultipartInputStream</tt> instance.
   */
  public MultipartInputStream(InputStream in, String ctype, int readlimit) {
    super((InputStream) in);
    m_ContentType = ctype;
    m_Limit = readlimit;
  }//constructor

  /**
   * Returns the name of this instance as <tt>String</tt>.
   * <p>(DataSource implementation)
   *
   * @return the name as <tt>String</tt>
   */
  public String getName() {
    return ("form_data");
  }//getName

  /**
   * Returns the content type of this instance as
   * <tt>String</tt>.
   * <p>(DataSource implementation)
   * <p><i><b>Note:</b>the <tt>String</tt> contains the parts border!</i>
   *
   * @return the content type as <tt>String</tt>
   */
  public String getContentType() {
    return m_ContentType;
  }//getContentType

  /**
   * Returns this <tt>MultipartInputStream</tt> instance as
   * <tt>InputStream</tt>.
   * <p>(DataSource implementation)
   *
   * @return this instance as <tt>InputStream</tt>.
   * @throws IOException if impossible.
   */
  public InputStream getInputStream()
      throws IOException {

    return (InputStream) this;
  }//getInputStream

  /**
   * Throws an IOException in this implementation, because
   * this instance represents a read-only <tt>DataSource</tt>.
   * <p>(DataSource implementation)
   *
   * @return an <tt>OutputStream</tt> instance for writing
   *         to this <tt>DataSource</tt>.
   * @throws IOException if impossible.
   */
  public OutputStream getOutputStream()
      throws IOException {

    throw new IOException("Cannot output to this source.");
  }//getOutputStream

  /**
   * Reads the next byte of data from the input stream. The value byte is
   * returned as an <tt>int</tt> in the range <tt>0</tt> to
   * <tt>255</tt>. If no byte is available because the end of the stream
   * has been reached, the value <tt>-1</tt> is returned. This method
   * blocks until input data is available, the end of the stream is detected,
   * or an exception is thrown.
   *
   * @return     the next byte of data, or <tt>-1</tt> if the end of the
   *             stream is reached.
   * @exception  IOException  if an I/O error occurs.
   */
  public int read() throws IOException {
    m_BytesRead++;
    checkLimit();
    return super.read();
  }//read

  /**
   * Reads up to <tt>len</tt> bytes of data from this input stream
   * into an array of bytes. This method blocks until some input is
   * available.
   * <p>
   * This method simply performs <tt>in.read(b, off, len)</tt>
   * and returns the result.
   *
   * @param      b     the buffer into which the data is read.
   * @param      off   the start offset of the data.
   * @param      len   the maximum number of bytes read.
   * @return     the total number of bytes read into the buffer, or
   *             <tt>-1</tt> if there is no more data because the end of
   *             the stream has been reached.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterInputStream#in
   */
  public int read(byte b[], int off, int len)
      throws IOException {
    m_BytesRead += len;
    checkLimit();
    return super.read(b, off, len);
  }//read

  /**
   * Checks if the size limit is exceeded, throwing
   * an IOException if so.
   *
   * @throws IOException if the limit is exceeded.
   */
  private void checkLimit()
      throws IOException {

    if (m_BytesRead > m_Limit) {
      throw new IOException("Input limit exceeded.");
    }
  }//checkLimit

}//class MultipartInputStream
