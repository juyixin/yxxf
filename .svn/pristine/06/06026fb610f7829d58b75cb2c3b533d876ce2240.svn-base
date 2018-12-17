/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util;

import net.wimpi.text.*;

/**
 * Utility class for handling entities.<br>
 * Exposes a method for encoding HTML unsafe characters
 * into entities. In the future it might also expose
 * a method for decoding entities into characters.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class EntityHandler {

  /*
  private static CharacterSubstitution c_Charsub=
      new CharacterSubstitution();
  private static Pattern c_Pattern=JwmaKernel.getReference()
      .getCharMatchPattern();
  */

  //class members
  //FIXME: get the Processor name from setting
  private static Processor m_EntityEncoder =
      ProcessingKernel.getReference().getProcessor("entityencoder");


  /**
   * Returns a <tt>String</tt> with all occurences of
   * HTML unsafe characters replaced by their respective entities.
   *
   * @param input the <tt>String</tt> to be encoded.
   * @return a <tt>String</tt> without HTML unsafe chars.
   */
  public static String encode(String input) {
    //outsourced
    return m_EntityEncoder.process(input);

    /*
       //do not waste cycles on nulls or empty strings
       if(input==null || input.equals("")) {
           return "";
       }

       PatternMatcher matcher=null;
       try {
           matcher=JwmaKernel.getReference().getMatcher();
           return Util.substitute(
                   matcher,
                   c_Pattern,
                   c_Charsub,
                   input,
                   Util.SUBSTITUTE_ALL
           );
       } catch (Exception ex) {
           return input;
       } finally {
           JwmaKernel.getReference().releaseMatcher(matcher);
       }
    */
  }//encode


}//EntityHandler
