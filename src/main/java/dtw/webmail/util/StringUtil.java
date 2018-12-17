/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;

/**
 * Class that contains static utility methods to
 * handle Strings.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class StringUtil {

  /**
   * Method that splits a string with delimited fields
   * into an array of strings.
   *
   * @param str String with delimited fields.
   * @param delim String that represents the delimiter.
   *
   * @return the String[] that contains all splitted fields.
   */
  public static String[] split(String str, String delim) {

    StringTokenizer strtok = new StringTokenizer(str, delim);
    String[] result = new String[strtok.countTokens()];

    for (int i = 0; i < result.length; i++) {
      result[i] = strtok.nextToken();
    }

    return result;
  }//split(String,String)

  /**
   * Method that joins an array of strings into a string
   * with delimited fields
   *
   * @param items an array of strings.
   * @param delim String that represents the delimiter.
   *
   * @return the String that contains the delimited list.
   */
  public static String join(String[] items, String delim) {
    StringBuffer sbuf = new StringBuffer();
    for (int i = 0; i < items.length; i++) {
      sbuf.append(items[i]);
      if (i < items.length - 1) {
        sbuf.append(delim);
      }
    }
    return sbuf.toString();
  }//join

  /**
   * This method is used to circumvent a bug (or feature) in the
   * URLClassLoader, which prevents it from being able to use file
   * url's with path strings which contain a  <tt>..</tt>
   * Example:
   * <tt>/usr/local/tomcat/bin/../webapps/webmail/WEB-INF/i18n/</tt>
   * does not properly work, whereas
   * <tt>/usr/local/tomcat/webapps/webmail/WEB-INF/i18n/</tt>
   * which represents the same (for the <tt>File</tt> class it makes
   * no difference whatsoever), but works.
   */
  public static String repairPath(String path) {
    int idx = -1;
    String newpath = "";

    idx = path.indexOf(File.separator + ".." + File.separator);
    if (idx > 0) {
      newpath = path.substring(0, idx);
      newpath = newpath.substring(0, newpath.lastIndexOf(File.separator));
      newpath += path.substring(idx + 3, path.length());
      return newpath;
    } else {
      return path;
    }
  }//repairPath

  public static boolean contains(String[] strs, String str) {
    for (int i = 0; i < strs.length; i++) {
      if(str.equals(strs[i])) {
        return true;
      }
    }
    return false;
  }//contains

  /**
   * Returns now, as <tt>String</tt> formatted
   * with <tt>DEFAULT_DATEFORMAT</tt>.
   *
   * @return now as formatted <tt>String</tt>.
   */
  public static String getFormattedDate() {
    return DEFAULT_DATEFORMAT.format(new Date());
  }//getFormattedDate


  /**
   * Defines a simple date format, to be used for returning the
   * formatted date.
   */
  public static SimpleDateFormat DEFAULT_DATEFORMAT =
      new SimpleDateFormat("E, dd.MMM yyyy hh:mm:ss (z)");

}//class StringUtil