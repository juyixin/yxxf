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

import org.apache.oro.text.regex.*;
import org.apache.log4j.Logger;

import net.wimpi.text.*;
import net.wimpi.text.std.*;


public class PlainTextUrl
    extends AbstractProcessor {

  //logging
  private static Logger log = Logger.getLogger(PlainTextUrl.class);

  private Pattern m_MatchPattern = null;
  private ResourcePool m_ResourcePool = null;

  public PlainTextUrl() {
    try {
      //prepare pattern
      m_MatchPattern =
          new Perl5Compiler().compile(URL_PATTERN);

      //get resourcepool
      m_ResourcePool =
          ProcessingKernel.getReference()
          .getResourcePool("patternmatchers");
    } catch (Exception ex) {
      throw new RuntimeException("Failed to construct processor.");
    }
  }//constructor

  public String getName() {
    return "plaintexturl";
  }//getName

  public String process(String str) {

    StringBuffer strbuf = new StringBuffer(str);
    //parse for urls and make them clickable
    int relinsertend = 0;
    int relinsertbegin = 0;
    int corr = 0;
    ProcessingResource resource =
        m_ResourcePool.leaseResource();
    PatternMatcher matcher =
        ((PatternMatchingResource) resource).getPatternMatcher();
    try {
      PatternMatcherInput input =
          new PatternMatcherInput(str);

      // Loop until there are no more matches left.
      while (matcher.contains(input, m_MatchPattern)) {
        // Since we're still in the loop,
        //fetch match that was found.
        MatchResult result = matcher.getMatch();
        //extracted url
        String url = result.toString();
        //now we got to work on the buffer
        relinsertend = (result.endOffset(0)) + corr;
        relinsertbegin = (result.beginOffset(0)) + corr;
        corr = corr + 4 + 25 + url.length();
        strbuf.insert(relinsertend, "</a>");
        strbuf.insert(relinsertbegin,
            "<a href='" + url + "' target='_top'>");
      }
    } finally {
      m_ResourcePool.releaseResource(resource);
    }
    return strbuf.toString();
  }//process

  public InputStream process(InputStream in)
      throws IOException {

    ByteArrayOutputStream bout =
        new ByteArrayOutputStream(in.available());

    byte[] buffer = new byte[8192];
    int amount = 0;
    while ((amount = in.read(buffer)) >= 0) {
      bout.write(buffer, 0, amount);
    }
    return new ByteArrayInputStream(
        process(bout.toString()).getBytes()
    );
  }//process

  private static final String URL_PATTERN =
      "(\\S)+://(\\S)+";


}//class PlainTextUrl