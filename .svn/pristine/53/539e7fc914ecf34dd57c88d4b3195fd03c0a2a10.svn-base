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
import java.text.BreakIterator;

import org.apache.log4j.Logger;
import net.wimpi.text.*;

/**
 * Implements a processor which wraps lines observing
 * word boundaries.
 *
 * @author August Detlefsen (augustd at codemagi.com), Dieter Wimberger
 */
public class LineWrapper
    extends AbstractProcessor {

  //logging
  private static Logger log = Logger.getLogger(LineWrapper.class);

  public String getName() {
    return "linewrapper";
  }//getName


  public final String process(String str) {
    return paragraphFormat(str, 80);
  }//process

  /**
   * Adds line breaks ('\n') to a paragraph of text
   *
   * @param paragraph the String to format.
   * @param colWidth the suggested column width (actual width will
   *                    be greater depending on word breaks).
   * @return String The input text formatted with line breaks every colWidth
   */
  private final String paragraphFormat(String input, int colWidth) {

    //set up the break iterator
    BreakIterator lines = BreakIterator.getLineInstance();
    lines.setText(input);

    //setup the output buffer
    StringBuffer output = new StringBuffer(input.length() + 100);

    int lastLineBreak = 0;
    int lastBreak = 0;
    int currentBreak = 0;

    StringBuffer currentWord = new StringBuffer();

    while (lines.next() != BreakIterator.DONE) {

      currentBreak = lines.current();

      //reset the currentWord buffer
      currentWord.setLength(0);
      currentWord.append(input.substring(lastBreak,
          currentBreak));

      //FIXME: try to find :// once, problem is that
      //the iterator takes : as boundary
      if (currentWord.toString().indexOf("http:") != -1
          || currentWord.toString().indexOf("ftp:") != -1
      ) {

        try {
          //don't chop URLs.
          //Problem is that Break iterator sees '.' as a good place to break.
          //Solution is to find the next space or line break
          int nextLine = input.indexOf("\n", currentBreak);
          int nextSpace = input.indexOf(" ", currentBreak);
          int nextBreak = nextLine >= nextSpace ? nextSpace :
              nextLine;


          if (nextBreak == -1) { //must be at end
            nextBreak = input.length();
          }

          //append everything up to the end of the url
          currentWord.append(input.substring(currentBreak,
              nextBreak));

          //set the break iterator forward
          for (int i = currentBreak; i != BreakIterator.DONE
              && i < nextBreak; i += 0) {
            //keep getting next until ...
            i = lines.next();
            currentBreak = i;
          }

        } catch (Exception e) {
          e.printStackTrace();
        }


      } else if (currentWord.toString().indexOf("\n") > -1) {
        //if there is already a line break, update
        lastLineBreak = currentBreak;

      } else if (currentBreak > lastLineBreak + colWidth) {
        //if column is getting too wide, break it

        currentWord.append("\n");
        //currentWord.append("!@!");
        lastLineBreak = currentBreak;
      }

      //append currentWord to the output buffer
      output.append(currentWord);

      //reset the break
      lastBreak = currentBreak;

    }

    return output.toString();
  }//paragraphFormat

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

}//class LineWrapper