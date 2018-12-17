/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util;

public interface MessageSortCriterias {

  public static final int RECEIVE_CHRONOLOGICAL = 0;
  public static final int RECEIVE_REVERSE_CHRONOLOGICAL = 1;
  public static final int SEND_CHRONOLOGICAL = 2;
  public static final int SEND_REVERSE_CHRONOLOGICAL = 3;
  public static final int RECEIVER_LEXOGRAPHICAL = 4;
  public static final int RECEIVER_REVERSE_LEXOGRAPHICAL = 5;
  public static final int SENDER_LEXOGRAPHICAL = 6;
  public static final int SENDER_REVERSE_LEXOGRAPHICAL = 7;
  public static final int NUMBER_NUMERICAL = 8;
  public static final int NUMBER_REVERSE_NUMERICAL = 9;
  public static final int DATE_CHRONOLOGICAL = 10;
  public static final int DATE_REVERSE_CHRONOLOGICAL = 11;
  public static final int WHO_LEXOGRAPHICAL = 12;
  public static final int WHO_REVERSE_LEXOGRAPHICAL = 13;

  public static final int[] EXUI_CRITERIAS = {
    DATE_CHRONOLOGICAL,
    DATE_REVERSE_CHRONOLOGICAL,
    NUMBER_NUMERICAL,
    NUMBER_REVERSE_NUMERICAL,
    WHO_LEXOGRAPHICAL,
    WHO_REVERSE_LEXOGRAPHICAL
  };

  public static final String[] EXUI_CRITERIAS_STR = {
    "messages.sort.oldestfirst", //"Date (oldest first)",
    "messages.sort.recentfirst", //"Date (recent first)",
    "messages.sort.numberincreasing", //"Number (increasing)",
    "messages.sort.numberdecreasing", //"Number (decreasing)",
    "messages.sort.wholexographical", //"Who (lexographical)",
    "messages.sort.whoreverselexographical", //"Who (reverse lex.)"
  };

}//interface MessageSortCriteria