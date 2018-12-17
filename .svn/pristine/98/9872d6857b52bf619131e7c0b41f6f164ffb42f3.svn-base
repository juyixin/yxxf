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

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.*;
import net.wimpi.text.*;
import net.wimpi.text.std.*;

public class EntityEncoder
    extends AbstractProcessor {

  //logging
  private static Logger log = Logger.getLogger(EntityEncoder.class);

  private Pattern m_MatchPattern = null;
  private ResourcePool m_ResourcePool = null;
  private CharacterSubstitution m_CharSub = null;

  public EntityEncoder() {
    try {
      //prepare pattern
      m_MatchPattern =
          new Perl5Compiler().compile(CHAR_PATTERN);
      //Prepare Substitution
      m_CharSub = new CharacterSubstitution();

      //get resourcepool
      m_ResourcePool =
          ProcessingKernel.getReference()
          .getResourcePool("patternmatchers");
    } catch (Exception ex) {
      throw new RuntimeException("Failed to construct processor.");
    }
  }//constructor

  public String getName() {
    return "entityencoder";
  }//getName

  public String process(String str) {

    //do not waste cycles on nulls or empty strings
    if (str == null || str.equals("")) {
      return "";
    }

    ProcessingResource resource =
        m_ResourcePool.leaseResource();
    PatternMatcher matcher =
        ((PatternMatchingResource) resource).getPatternMatcher();
    try {

      return Util.substitute(
          matcher,
          m_MatchPattern,
          m_CharSub,
          str,
          Util.SUBSTITUTE_ALL
      );
    } finally {
      m_ResourcePool.releaseResource(resource);
    }
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


  private static final String CHAR_PATTERN =
      "([^\\n\\t !\\#\\$%\\'-;=?-~])";


}//class EntityEncoder