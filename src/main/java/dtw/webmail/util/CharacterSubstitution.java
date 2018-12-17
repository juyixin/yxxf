/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util;

import org.apache.oro.text.regex.*;

/**
 * Class that implements a Substitution, replacing special
 * HTML unsafe characters with their entity representations.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class CharacterSubstitution implements Substitution {


  /**
   * Appends the substitution to a buffer containing the original input
   * with substitutions applied for the pattern matches found so far.
   * For maximum flexibility, the original input as well as the
   * PatternMatcher and Pattern used to find the match are included as
   * arguments.  However, you will almost never find a need to use those
   * arguments when creating your own Substitution implementations.
   * <p>
   * For performance reasons, rather than provide a getSubstitution method
   * that returns a String used by Util.substitute, we have opted to pass
   * a StringBuffer argument from Util.substitute to which the Substitution
   * must append data.  The contract that an appendSubstitution
   * implementation must abide by is that the appendBuffer may only be
   * appended to.  appendSubstitution() may not alter the appendBuffer in
   * any way other than appending to it.
   * <p>
   * This method is invoked by Util.substitute every time it finds a match.
   * After finding a match, Util.substitute appends to the appendBuffer
   * all of the original input occuring between the end of the last match
   * and the beginning of the current match.  Then it invokes
   * appendSubstitution(), passing the appendBuffer, current match, and
   * other information as arguments.  The substitutionCount keeps track
   * of how many substitutions have been performed so far by an invocation
   * of Util.substitute.  Its value starts at 1 when the first substitution
   * is found and appendSubstitution is invoked for the first time.  It
   * will NEVER be zero or a negative value.
   * <p>
   * @param appendBuffer The buffer containing the new string resulting
   * from performing substitutions on the original input.
   * @param match The current match causing a substitution to be made.
   * @param substitutionCount  The number of substitutions that have been
   *  performed so far by Util.substitute.
   * @param originalInput The original input upon which the substitutions are
   * being performed.
   * @param matcher The PatternMatcher used to find the current match.
   * @param pattern The Pattern used to find the current match.
   */
  public void appendSubstitution(StringBuffer appendBuffer,
                                 MatchResult match,
                                 int substitutionCount,
                                 PatternMatcherInput originalInput,
                                 PatternMatcher matcher,
                                 Pattern pattern) {

    char c = match.toString().charAt(0);
    appendBuffer.append(char2Entity(c));

  }//appendSubstitution

  /**
   * Translates (a few) chars to their corresponding entity.
   */
  private String char2Entity(char c) {
    switch (c) {
      case AMPERSAND_CHAR:
        return AMPERSAND_ENTITY;
      case GT_CHAR:
        return GT_ENTITY;
      case LT_CHAR:
        return LT_ENTITY;
      case QUOT_CHAR:
        return QUOT_ENTITY;
      default:
        return "" + c;
    }

  }//char2Entity


  private static final char AMPERSAND_CHAR = '&';
  private static final String AMPERSAND_ENTITY = "&amp;";
  private static final char GT_CHAR = '>';
  private static final String GT_ENTITY = "&gt;";
  private static final char LT_CHAR = '<';
  private static final String LT_ENTITY = "&lt;";
  private static final char QUOT_CHAR = '"';
  private static final String QUOT_ENTITY = "&quot;";

}//CharacterSubstitution
