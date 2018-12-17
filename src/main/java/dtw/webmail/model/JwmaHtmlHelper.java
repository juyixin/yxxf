/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dtw.webmail.JwmaKernel;


/**
 * Class implementing the JwmaHtmlHelper.
 * <p>Actually represents an utility class that helps
 * to keep complex logic out of the views.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaHtmlHelper
    implements Serializable {

  //logging
  private static Logger log = Logger.getLogger(JwmaHtmlHelper.class);

  public JwmaHtmlHelper() {
  }//constructor

  public String displayPartInlined(HttpSession session,
                                   JwmaMessagePart part,
                                   JwmaPreferences prefs, String from) {

    StringBuffer sbuf = new StringBuffer();
    JwmaMessagePartImpl msgpart = (JwmaMessagePartImpl) part;
    log.debug("displaying part inlined type=" + msgpart.getContentType());
    try {
      //handle by type
	  if(msgpart.getName() != null){
    	  sbuf.append(getPartDescription(msgpart, from));
      } else {
	      if (msgpart.isMimeType("text/plain")) {
	        //log.debug("textcontent="+msgpart.getTextContent());
	        sbuf.append("<pre>");
	        sbuf.append(
	            prefs.getMessageProcessor().process(
	                msgpart.getTextContent()
	            )
	        );
	        sbuf.append("</pre>");
	      } else if (msgpart.isMimeType("image/*")) {
	        sbuf.append("<img border=\"0\" src=\"")
	            //.append(JwmaKernel.getReference().getMainControllerUrl())
	            .append("?acton=message&todo=displaypart&number=")
	            .append(part.getPartNumber())
	            .append("\">");
	      } else if (msgpart.isMimeType("text/directory")
	          || msgpart.isMimeType("application/directory")) {
	     
	
	      } else if(msgpart.isMimeType("text/html")){
	    	  if(msgpart.getName() != null){
	    		  sbuf.append(getPartDescription(msgpart, from));
	    	  } else {
	    		  sbuf.append("<pre>");
	  	       /* sbuf.append(
	  	            prefs.getMessageProcessor().process(
	  	                msgpart.getTextContent()
	  	            )
	  	        );*/
	    		  sbuf.append(msgpart.getTextContent());
	  	        sbuf.append("</pre>");
	    	  }
	    	  
	      } else {
	    	  sbuf.append(getPartDescription(msgpart, from));
	      }
      }
      return sbuf.toString();
    } catch (Exception ex) {
      log.debug("displayPartInlined()", ex);
      return "Failed to display part inlined.";
    }
  }//displayPartInlined

  public String getPartDescription(JwmaMessagePart part, String from) {

    StringBuffer sbuf = new StringBuffer("<div class='row-fluid' id=");
    sbuf.append(part.getPartNumber())
    	.append(" ><p>")
    	.append("<b>Filename : </b>")
        .append("<a href=\"javascript:void(0);\"")
        .append(" onclick=\"")
        .append("downloadAttachments(")
        .append(part.getPartNumber())
        .append(")\">")
        .append(part.getName())
        .append("</a>(<i>")
        .append(getSizeString(part.getSize()))
        .append("</i>)\t&nbsp;&nbsp;&nbsp;");
        if(from.equalsIgnoreCase("compose")){
        	sbuf.append("<b>")
            .append("<a href=\"javascript:void(0);\"")
            .append(" onclick=\"")
            .append("removeFileAttachments(")
            .append(part.getPartNumber())
            .append(")\">")
            .append("Remove")
        	.append("</a>  </b>")
        	.append("\n");
        }
        sbuf.append("</p></div>\n");
    return sbuf.toString();
  }//getPartDescription

  public String getSizeString(int size) {
    int steps = 0;
    while (size > 1024) {
      size = size / 1024;
      steps++;
    }
    if (steps == 0) {
      return size + " bytes";
    } else if (steps == 1) {
      return size + " kB";
    } else if (steps == 2) {
      return size + " MB";
    } else {
      return "Huge ;)";
    }
  }//getSizeString

}//class JwmaHtmlHelperImpl
