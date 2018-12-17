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

import dtw.webmail.model.*;

/**
 * Utility class providing <tt>Comparator</tt>'s for
 * sorting messages applying different criterias.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class MessageSortingUtil {


  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the receive or sent date chronological (i.e. oldest first).
   */
  public static final Comparator DATE_CHRONOLOGICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg1.getDate().compareTo(
              msg2.getDate()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the receive or sent date reverse to chronological
   * (i.e. most recent first).
   */
  public static final Comparator DATE_REVERSE_CHRONOLOGICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg2.getDate().compareTo(
              msg1.getDate()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the receive date chronological (i.e. oldest first).
   */
  public static final Comparator RECEIVE_CHRONOLOGICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg1.getReceivedDate().compareTo(
              msg2.getReceivedDate()
          );
        }//compare(Object,Object)
      };
  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the receive date reverse to chronological
   * (i.e. most recent first).
   */
  public static final Comparator RECEIVE_REVERSE_CHRONOLOGICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg2.getReceivedDate().compareTo(
              msg1.getReceivedDate()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the send date chronological (i.e. oldest first).
   */
  public static final Comparator SEND_CHRONOLOGICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg1.getSentDate().compareTo(
              msg2.getSentDate()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the send date reverse to chronological
   * (i.e. most recent first).
   */
  public static final Comparator SEND_REVERSE_CHRONOLOGICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg2.getSentDate().compareTo(
              msg1.getSentDate()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the sender or receiver string lexographical.
   */
  public static final Comparator WHO_LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg1.getWho().compareTo(
              msg2.getWho()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the sender or receiver string reverse lexographical.
   */
  public static final Comparator WHO_REVERSE_LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg2.getWho().compareTo(
              msg1.getWho()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the sender string lexographical.
   */
  public static final Comparator SENDER_LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg1.getFrom().compareTo(
              msg2.getFrom()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the sender string reverse lexographical.
   */
  public static final Comparator SENDER_REVERSE_LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg2.getFrom().compareTo(
              msg1.getFrom()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the receiver string lexographical.
   */
  public static final Comparator RECEIVER_LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg1.getTo().compareTo(
              msg2.getTo()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the receiver string reverse lexographical.
   */
  public static final Comparator RECEIVER_REVERSE_LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfo msg1 = (JwmaMessageInfo) o1;
          JwmaMessageInfo msg2 = (JwmaMessageInfo) o2;
          return msg2.getTo().compareTo(
              msg1.getTo()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the message number numerical (i.e. lowest first).
   */
  public static final Comparator NUMBER_NUMERICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfoImpl msg1 = (JwmaMessageInfoImpl) o1;
          JwmaMessageInfoImpl msg2 = (JwmaMessageInfoImpl) o2;
          return msg1.getNumberForSort().compareTo(
              msg2.getNumberForSort()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the message number reverse numerical (i.e. highest first).
   */
  public static final Comparator NUMBER_REVERSE_NUMERICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaMessageInfoImpl msg1 = (JwmaMessageInfoImpl) o1;
          JwmaMessageInfoImpl msg2 = (JwmaMessageInfoImpl) o2;
          return msg2.getNumberForSort().compareTo(
              msg1.getNumberForSort()
          );
        }//compare(Object,Object)
      };

  /**
   * Defines the relation between a given criteria and a
   * comparator.
   */
  public static final Comparator[] CRITERIA_COMPARATOR = {
    RECEIVE_CHRONOLOGICAL, //0
    RECEIVE_REVERSE_CHRONOLOGICAL, //1
    SEND_CHRONOLOGICAL, //2
    SEND_REVERSE_CHRONOLOGICAL, //3
    RECEIVER_LEXOGRAPHICAL, //4
    RECEIVER_REVERSE_LEXOGRAPHICAL, //5
    SENDER_LEXOGRAPHICAL, //6
    SENDER_REVERSE_LEXOGRAPHICAL, //7
    NUMBER_NUMERICAL, //8
    NUMBER_REVERSE_NUMERICAL, //9
    DATE_CHRONOLOGICAL, //10
    DATE_REVERSE_CHRONOLOGICAL, //11
    WHO_LEXOGRAPHICAL, //12
    WHO_REVERSE_LEXOGRAPHICAL			//13
  };


}//class MessageSortingUtil