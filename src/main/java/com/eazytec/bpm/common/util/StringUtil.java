
package com.eazytec.bpm.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.eazytec.exceptions.EazyBpmException;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class StringUtil {

	// ~ Methods ---------------------------------------------------------------

	/**
	 * DOCUMENT ME!
	 *
	 * @param s
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */

	public StringUtil() {
	}

	public static boolean isEmptyString(String s) {
		 if ((s == null) || (s.trim().length() == 0)) {
		 return (true);
		 } else {
		 return (false);
		 }
	 }

	public static boolean isEmptyString(final Object object) {
		if ((object == null) || (object.toString().trim().length() == 0)) {
			return true;
		}
		return false;
	}

	/**
	 * Compares 2 string arrays and determines if they have the same elements or
	 * not. Returns true if both the arrays have the same strings. Returns false
	 * if the arrays have different strings.
	 *
	 * @param a
	 *            DOCUMENT ME!
	 * @param b
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static boolean compareStringArrays(final String[] a, final String[] b) {
		if (a.length != b.length) {
			return (false);
		} else {
			Arrays.sort(a);
			Arrays.sort(b);

			return Arrays.equals(a, b);
		}
	}

	/**
	 * Compares 2 lists of strings and determines if they have the same elements
	 * or not. Returns true if both the lists have the same strings. Returns
	 * false if the lists have different strings.
	 *
	 * @param a
	 *            DOCUMENT ME!
	 * @param b
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static boolean compareStringLists(final List a, final List b) {
		if (a.size() != b.size()) {
			return (false);
		} else {
			return (a.containsAll(b));
		}
	}

	/**
	 * Check the value exists in the given string array
	 *
	 * @param values
	 *            -String Array
	 * @param values
	 *            -String value
	 *
	 * @return DOCUMENT ME!
	 */
	public static boolean containsValue(final String[] values,
			final String value) {
		if (values == null) {
			return false;
		}

		for (int i = 0; i < values.length; i++) {
			if (value.equals(values[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * If the value exists in the given string array
	 *
	 * @param s -
	 *            Array of String
	 * @param beginIndex
	 *            a int value
	 *
	 * @return boolean
	 */
	public static boolean containsValues(final String[] s, final int beginIndex) {
		if ((s == null) || (s.length == 0)) {
			return false;
		}

		if (s.length <= beginIndex) {
			return false;
		}

		boolean flag = false;

		for (int i = beginIndex; i < s.length; i++) {
			if (!isEmptyString(s[i])) {
				flag = true;

				break;
			}
		}

		return flag;
	}

	/**
	 * Takes an argument in set-syntax, see evaluateSet, and returns the number
	 * of characters present in the specified string.
	 *
	 * @param str -
	 *            a String value
	 *
	 * @return int
	 */
	public static int countDigits(final String str) {
		String set = "0-9";

		return org.apache.commons.lang.CharSetUtils.count(str, set);
	}

	/**
	 * Returns a string representation of the stack trace of the given exception
	 *
	 * @param th
	 *            DOCUMENT ME!
	 *
	 * @return String
	 */
	public static String exceptionToString(final Throwable th) {
		String stTrace = "";

		try {
			StringWriter sout = new StringWriter();
			PrintWriter out = new PrintWriter(sout);
			th.printStackTrace(out);
			out.close();
			sout.close();
			stTrace = sout.toString();
			sout = null;
			out = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return stTrace;
	}

	/**
	 * Given a string replace all the occurences of key with value
	 *
	 * @param expr -
	 *            String value
	 * @param key -
	 *            String value
	 * @param value -
	 *            String value
	 *
	 * @return String
	 */
	public static String findAndReplaceAll(final String expr1,
			final String key, final String value) {
		boolean done = false;
		String expr = expr1;
		int idx = 0;

		while (!done) {
			idx = expr.indexOf(key, idx);

			if (idx >= 0) {
				expr = expr.substring(0, idx) + value
						+ expr.substring(idx + key.length());
				idx += value.length();
			} else {
				done = true;
			}
		}

		return expr;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param argv
	 *            DOCUMENT ME!
	 */
	public static void main(final String[] argv) {
		try {
			/*
			 * String input = argv[0];  List inputList = new
			 * ArrayList(); inputList.add("userid1"); inputList.add("radnaray");
			 * inputList.add("mvivekan"); inputList.add("useridwithlength");
			 * inputList.add(""); inputList.add("");
			 *
			  String[] test = {"", "", "", "", "",
			 * "", "", "", ""}; 
			 *
			 * String trace = exceptionToString(new Exception("A new
			 * exception"));  java.util.HashMap map =
			 * new java.util.HashMap(); map.put("userId", "arthur");
			 * map.put("firstName", "Arthur " ); map.put("LastName", "Levitt ");
			 * map.put("reg key ", "PRS2004");
			 *
			 */

			/*
			 * List a = new ArrayList(); a.add("a"); a.add("b"); List b = new
			 * ArrayList(); b.add("a"); b.add("b");
			 * if(compareStringLists(a, b)) {
			 */
			String[] a = { "a", "b" };
			String[] b = { "b", "a" };

			// if (compareStringArrays(a, b)) {
			// } else {
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		List test = new ArrayList();

		if (test != null) {
			String[] s = new String[test.size()];
			s = (String[]) test.toArray(s);
		}

		System.exit(0);
	}

	/**
	 * This method read the String From File
	 *
	 * @param path
	 *            -a String value
	 *
	 * @return String
	 */
	public static String readStringFromFile(final String path) {
		String str = null;

		try {
			String temp = null;
			File inputFile = new File(path);
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
			StringWriter sw = new StringWriter();

			while ((temp = br.readLine()) != null) {
				sw.write(temp);
			}

			str = sw.toString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (str);
	}

	/**
	 * It trims the spaces from the string... too..
	 *
	 * @param s -
	 *            a String value
	 * @param delim -
	 *            a String value
	 *
	 * @return (res) Array of strings
	 */
	public static String[] stokenize(final String s, final String delim) {
		List list = new ArrayList();
		StringTokenizer st = new StringTokenizer(s, delim);

		while (st.hasMoreTokens()) {
			String str = st.nextToken().trim();

			if (str.length() != 0) {
				list.add(str);
			}
		}

		String[] res = (String[]) list.toArray(new String[0]);

		return res;
	}

	/**
	 * Tokenizes the given string and remove the Duplicates with the
	 * specifieddelimeter
	 *
	 * @param s -
	 *            a String value
	 * @param delim -
	 *            a String value
	 *
	 * @return Array of tokens
	 */
	public static String[] stokenizeRemoveDup(final String s, final String delim) {
		List tl = tokenizeRemoveDup(s, delim);
		String[] ret = new String[tl.size()];

		return (String[]) tl.toArray(ret);
	}

	/**
	 * Strips the input string of all non alpha-numeric characters
	 *
	 * @param s -
	 *            a String value
	 *
	 * @return String
	 */
	public static String stripNonAlphaNumericChars(final String s) {
		if ((s == null) || (s.length() == 0)) {
			return s;
		}

		StringBuffer tmp = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (Character.isLetterOrDigit(c)) {
				tmp.append(c);
			}
		}

		if (tmp.length() > 0) {
			return tmp.toString();
		}

		return s;
	}

	/**
	 * Returns an array of size 1, containing the specified string value
	 *
	 * @param s -
	 *            a String value
	 *
	 * @return array of String
	 */
	public static String[] toArray(final String s) {
		String[] arr = new String[1];
		arr[0] = s;

		return arr;
	}

	/**
	 * Returns a comma seperated list of the strings in s
	 *
	 * @param s -
	 *            String array to be formated
	 *
	 * @return String
	 */
	public static String toCSL(final String[] s) {
		StringBuffer buf = new StringBuffer("");

		if ((s != null) && (s.length > 0)) {
			for (int i = 0; i < s.length; i++) {
				if (!isEmptyString(s[i])) {
					buf.append(s[i]);

					if ((i < (s.length - 1)) && containsValues(s, i + 1)) {
						buf.append(", ");
					}
				}
			}
		}

		return buf.toString().trim();
	}

	/**
	 * Returns a comma seperated list of the strings in s
	 *
	 * @param s -
	 *            String array to be formated
	 * @param delimiter -
	 *            a String value
	 *
	 * @return String
	 */
	public static String toCSL(final String[] s, final String delimiter) {
		StringBuffer buf = new StringBuffer("");

		if ((s != null) && (s.length > 0)) {
			if (!isEmptyString(s[0])) {
				buf.append(s[0]);
			}

			for (int i = 1; i < s.length; i++) {
				if (!isEmptyString(s[i])) {
					buf.append(delimiter);
					buf.append(s[i]);
				}
			}
		}

		return buf.toString().trim();
	}

	/**
	 * Returns a delimiter seperated list of the strings in s
	 *
	 * @param l
	 *            to be formated
	 * @param delimiter -
	 *            String value
	 *
	 * @return String
	 */
	public static String toCSL(final List l, final String delimiter) {
		String[] tmp = new String[1];

		return toCSL((String[]) l.toArray(tmp), delimiter);
	}

	/**
	 * Returns a comma seperated list of the strings in s
	 *
	 * @param list
	 *            DOCUMENT ME!
	 *
	 * @return String
	 */
	public static String toCSL(final List list) {
		String[] s = new String[list.size()];
		s = (String[]) list.toArray(s);

		return toCSL(s);
	}

	/**
	 * Converts each string in the input list to upper case string
	 *
	 * @param strings
	 *            of Strings
	 *
	 * @return List of Strings - all upper case
	 */
	public static List toUpper(final List strings) {
		List upper = new ArrayList(strings.size());

		for (int i = 0; i < strings.size(); i++) {
			upper.add(((String) strings.get(i)).toUpperCase());
		}

		return upper;
	}

	/**
	 * Tokenizes the given string with the specified delimeter and trims the
	 * spaces from each resulting token string
	 *
	 * @param s -
	 *            a String value
	 * @param delim -
	 *            a String value
	 *
	 * @return (res) List of tokens
	 */
	public static List tokenize(final String s, final String delim) {
		List res = new ArrayList();
		if (null != s && null != delim && s.trim().length() > 0) {
			StringTokenizer strTokenizer = new StringTokenizer(s, delim);
	
			while (strTokenizer.hasMoreTokens()) {
				res.add(strTokenizer.nextToken().trim());
			}
		}
		return res;
	}

	/**
	 * Tokenizes the given string and removes the Duplicate with the specified
	 * delimeter and trims the spaces from each resulting token string
	 *
	 * @param s -
	 *            a String value
	 * @param delim -
	 *            a String value
	 *
	 * @return (res) List of tokens
	 */
	public static List tokenizeRemoveDup(final String s, final String delim) {
		List res = new ArrayList();
		StringTokenizer strTokenizer = new StringTokenizer(s, delim);

		while (strTokenizer.hasMoreTokens()) {
			String nextToken = strTokenizer.nextToken().trim();

			if (res.indexOf(nextToken) == -1) {
				res.add(nextToken);
			}
		}

		return res;
	}

	// ~ Methods ---------------------------------------------------------------

	public static String createFormName(String rawFormName) {

		List formTokens = tokenize(rawFormName, " ");

		if (formTokens == null || formTokens.size() <= 1) {

			return convertFirstLetterToUpperCase(rawFormName);
		} else {
			StringBuffer formName = new StringBuffer();

			for (int i = 0; i < formTokens.size(); i++) {

				String tokenString = (String) formTokens.get(i);

				formName.append(convertFirstLetterToUpperCase(tokenString));
			}
			return formName.toString();
		}
	}

	private static String convertFirstLetterToUpperCase(String rawFormName) {

		return StringUtils.capitalize(rawFormName);
	}

	public static String firstToUpperCase(String string) {
		String post = string.substring(1, string.length());
		String first = ("" + string.charAt(0)).toUpperCase();
		return first + post;
	}

	public static String firstToLowerCase(String string) {
		String post = string.substring(1, string.length());
		String first = ("" + string.charAt(0)).toLowerCase();
		return first + post;
	}

	public static String replaceOldNameWithNewNameInExpression(
			String expression, String oldName, String newName) {
		int oldNameLength = oldName.length();
		int newNameLength = newName.length();
		for (int i = 0; i < expression.length();) {
			int length = expression.length();
			if (expression.contains(oldName)) {
				int pos1 = expression.indexOf(oldName, i);
				int pos2 = pos1 + oldName.length();
				if (pos1 != -1) {
					if (pos1 == 0) {
						if (pos2 == length) {
							expression = expression.substring(0, pos1)
									+ newName + expression.substring(pos2);
							break;
						}
						if (length > pos2
								&& !isAlphaNumeric(expression.charAt(pos2))) {
							expression = expression.substring(0, pos1)
									+ newName + expression.substring(pos2);
							i = pos1 + newNameLength;
							continue;
						}
					} else if (pos2 >= length) {
						if (!isAlphaNumeric(expression.charAt(pos1 - 1))) {
							expression = expression.substring(0, pos1)
									+ newName + expression.substring(pos2);
							i = pos1 + newNameLength;
							continue;
						}
					} else if (!isAlphaNumeric(expression.charAt(pos1 - 1))) {
						if (!isAlphaNumeric(expression.charAt(pos2))) {
							expression = expression.substring(0, pos1)
									+ newName + expression.substring(pos2);
							i = pos1 + newNameLength;
							continue;
						}
					}
					i = pos1 + 1;
					continue;
				}
				i = i + oldNameLength;
			} else {
				break;
			}
		}
		return expression;
	}

	public static boolean isAlphaNumeric(final char c) {
		if ((c >= 'a') && (c <= 'z')) {
			return true;
		}
		if ((c >= 'A') && (c <= 'Z')) {
			return true;
		}
		if ((c >= '0') && (c <= '9')) {
			return true;
		}
		return false;
	}

	public static String getAppendedStringWithSpace(String oldValue,
			String newValue) {
		if (oldValue == null || oldValue.trim().length() == 0) {
			return newValue;
		}
		return (oldValue + " " + newValue);
	}

	public static String convertArrayToString(Object[] values,
			String delim, boolean quotesNeeded) {
		if (values == null || values.length == 0) {
			return null;
		}
		String value = "";
		if(quotesNeeded) {
			for (int i = 0; i < values.length; i++) {
				if (i == 0) {
					if (values[i] != null && values[i].toString().length() > 0) {
						value = "'" + values[i] + "'";
					}
				} else {
					if (values[i] != null && values[i].toString().length() > 0) {
						value += delim + "'" + values[i] + "'";
					}
				}
			}
		} else {
			for (int i = 0; i < values.length; i++) {
				if (i == 0) {
					if (values[i] != null && values[i].toString().length() > 0) {
						value = (String) values[i];
					}
				} else {
					if (values[i] != null && values[i].toString().trim().length() > 0) {
						value += delim + values[i];
					}
				}
			}
		}
		return value;
	}

	/**
	 * <p>
	 * Removes all &nbsp; and replaces all html line breaking tags with "\n"
	 * </p>
	 * 
	 * @param message
	 *            a String object
	 * @return String formatted message
	 */
	public static String getNonHTMLMessage(String message) {
		if (message != null && message.trim().length() > 0) {

			message = message.replaceAll("&nbsp;", "");
			message = message.replaceAll("<BR>", "\n");
			message = message.replaceAll("<br>", "\n");
		}
		return message;
	}

	public static String getHTMLMessage(String message) {
		if (message != null && message.trim().length() > 0) {
			message = message.replaceAll("\n", "<BR>");
		}
		return message;
	}

	/**
	 * Insert a <BR> into every nth position in the given string.
	 * Used for rendering long text elements. Using WRAP attribute is another alternative,
	 * but firefox doesn't honor that tag.
	 * 
	 * @param strToBreak String to break 
	 * @param breakLength After every breakLength no. of chars, a <BR> will be inserted
	 * 
	 */
	public static String breakStringByBr(String strToBreak, int breakLength) {

		if(breakLength == 0) {
			return strToBreak;
		}
		StringBuffer brokenString = new StringBuffer();

		String whatIsLeft = "";
		String strAt = " ";
//		while (whatIsLeft.length() > breakLength) {			
//			brokenString.append(whatIsLeft.substring(0, breakLength)).append(
//						"<BR>");
//			whatIsLeft = whatIsLeft.substring(breakLength, whatIsLeft.length());
//		}
		if(strToBreak.length() > 0){
			for(int i=0;i<strToBreak.length();i+=breakLength){
				if(strToBreak.length() > i+breakLength){
					strAt = Character.toString(strToBreak.charAt(i+breakLength));
				}else{
					strAt = " ";
				}
				if(!strAt.equalsIgnoreCase(" ")){
					if(strToBreak.substring(i, i+breakLength).contains(" ") || strToBreak.substring(i, i+breakLength).contains("<br>")){
						String stringRight = null;
						if(i+breakLength+breakLength > strToBreak.length()){
							stringRight = strToBreak.substring(i+breakLength,strToBreak.length());
						}else{
							stringRight = strToBreak.substring(i+breakLength,i+breakLength+breakLength);
						}
						if(stringRight.contains(" ") || stringRight.contains("<br>")){
							whatIsLeft += strToBreak.substring(i, i+breakLength);
						}else{
							if(stringRight.length() < breakLength){
								whatIsLeft += strToBreak.substring(i, i+breakLength);
							}else{
								whatIsLeft += strToBreak.substring(i, i+breakLength)+"<br>";
							}
						}
					}else{
						whatIsLeft += strToBreak.substring(i, i+breakLength)+"<br>";
					}				
				}else{
					if(i+breakLength > strToBreak.length()){
						whatIsLeft += strToBreak.substring(i, strToBreak.length());
					}else{
						whatIsLeft += strToBreak.substring(i, i+breakLength);
					}
				}
			}
		}
		brokenString.append(whatIsLeft);
		return brokenString.toString();
	}

	/** 
	 * Break to string to maxLineLenght and append "...". For displaying titles that are very long.
	 * @param strToBreak
	 * @param maxLineLength
	 * @return
	 */
	public static String breakString(String strToBreak, int maxLineLength) {
		if(null !=strToBreak && strToBreak.length() > maxLineLength) {
			return strToBreak.substring(0, maxLineLength) + "...";
		}
		return strToBreak;
	}
	
	public static boolean isURL(String value) {
		String parrern ="^(ftp|http|https)://([^/]+)(/.*)?";
		Pattern p = Pattern.compile(parrern);
		//value = value.replaceAll("<br>", "");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	public static boolean isEmail(String value) {
		String parrern = "[a-z0-9\\.\\-\\_]+@([a-z0-9\\-\\_]+\\.)+(com|org|info|net|[a-z]{2,4})";
		Pattern p = Pattern.compile(parrern);
		//value = value.replaceAll("<br>", "");
		//value = value.replaceAll("\n", "");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	//Pull all links (urls) from the text
	public static ArrayList<String> pullUrl(String text) {
		ArrayList<String> links = new ArrayList<String>();
		 
		String regex = "\\(?\\b(http|https|ftp://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while(m.find()) {
		String urlStr = m.group();
		if (urlStr.startsWith("(") && urlStr.endsWith(")"))
		{
		urlStr = urlStr.substring(1, urlStr.length() - 1);
		}
		links.add(urlStr);
		}
		return links;
	}
	
	//Pull all email ids from the value
	public static ArrayList<String> pullEmail( String value) {
		ArrayList<String> emailIdList = new ArrayList<String>();
		Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);
		while (m.find()){
		     emailIdList.add(m.group()); 
		}
		return emailIdList;
	}
	
	/**
	 * This method will return string of array values into set of string values.
	 * This method implemented for MULTISELECT Field . 
	 * @param groupIdStr
	 * @return
	 */
	 public static Set getStringToSet(String[] groupIdStr){
	  		Set keywordSet = new HashSet();
	            if ((groupIdStr != null) && (groupIdStr.length> 0)) {
	                for(int i=0;i<groupIdStr.length;i++){
	                	keywordSet.add(groupIdStr[i]);
	                }
	            }
	            return keywordSet;
	 }
	 
	 public static String[] convertArrayToString(String fieldString , String delimeter){
		String[] returnString = fieldString.split(delimeter);
		return returnString;
	 }
	
	/**
	 * change the string letter specified at index to lowercase
	 * @param string
	 * @param index
	 * @return
	 */
	public static String toLowerCase(String string, int index) {
		char keyChars[] = string.toCharArray();
		char letter =  keyChars[index];
		String smallLetter = String.valueOf(letter).toLowerCase();
		keyChars[index] = smallLetter.toCharArray()[0];
		return new String(keyChars);
	}
	
	public static String stripControlCharacters(String xmlString, String numberType) {
		if(null == xmlString || "".equals(xmlString)) {
			return "";
		}
		String controlChar ="";
		/**	
		* 0 to 31 decimals represents the ascii values of the Control Characters.(ex.NUL,SOH,STX,ETX,....RS,US.)
		* Since UTF-8 does not support the control characters eventhough it encodes control characters.
		* So we are truncating these control characters from the xmlstream before posting to the xmlinterface.	
		*/
		if ("decimal".equalsIgnoreCase(numberType)) {
			for(int i=0; i<=31; i++) {
				if(i != 9 && i != 10 && i != 13) {	//except control characters horizontal tab(TAB), new line(LF) and carriage return(CR)
					controlChar = "&#" + i + ";";
					xmlString =xmlString.replace(controlChar, "");
				}
			}
		} else if ("hexadecimal".equalsIgnoreCase(numberType)) {
			String hexaNos[] = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"
					+"20","21","22","23","24","25","26","27","28","29","30","31"};
			for(int i=0; i < hexaNos.length; i++) {
				controlChar = "&#x" + i + ";";
				xmlString =xmlString.replace(controlChar, "");
			}
		}
		return xmlString;
	}
	
	/**
	 * Matches with the standard mobile number pattern
	 * @param number the number to be checked
	 * @return matches or not
	 */
	public static boolean isValidMobileNo(final String number) {
		int length=number.length();
		String mobileFormat = "^[+]?(91[\\-\\s]?)?[0-9]{10}+$";
		Pattern pattern = Pattern.compile(mobileFormat);  
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();		
	}
	
	/**
	 * Return first letter as uppercase.
	 * eg: fieldName to FieldName
	 * @param fieldName
	 * @return
	 */
	public static String getFirstUpper(String fieldName){
		if (!isEmptyString(fieldName)) {
			char[] stringArray = fieldName.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			return new String(stringArray);
		}else{
			return fieldName;
		}
	}
	
	/**
	 * Convert List<String> into String separated by comma with Single Code i.e. 'abcd','defg' 
	   this may be used IN Query to fetch the records
	 * @param listOfString List<String> to be converted
	 * @return String Converted String
	 */
   public static String getCommaSeparatedString(List<String> listOfString){
	   StringBuffer stringBuffer = new StringBuffer();
		int size = 0;
		Set<String> stringSet = new HashSet<String>(listOfString);
	   for (String element : stringSet) {
				if (stringBuffer.length() > 0) {
					stringBuffer.append(",");
				}
				stringBuffer.append("'");
				stringBuffer.append(element);
				stringBuffer.append("'");
				size = size + 1;
		}
	   return stringBuffer.toString();
   }
   
   /**
    * @author babuk
    * Gets the list of node values from the given content.
    * Node values will be added in list when value is between the opener and closer.
    * @param content
    * @param opener
    * @param closer
    * @return list of nodeValue
    */
   public static List<String> getNodeValue(String content, String opener, String closer){
	   List<String> resultList = new ArrayList<String>();
	   while(content.contains(opener) && content.contains(closer)){
			String recursiveString = content.substring(content.indexOf(opener)+opener.length());
			resultList.add(recursiveString.substring(0, recursiveString.indexOf(closer)).trim());
			content = recursiveString.substring(recursiveString.indexOf(closer)+closer.length());
	   }
	   return resultList;
		
   }
   
   /**
    * Converts numeric character reference (NCR) to character entity reference.
    * 
    * @param str - string in NCR format.
    * 
    * @return string in unicode character entity reference.
    * @throws EazyBpmException
    */
   public static String unescapeHtmlUnicode(String str)throws EazyBpmException{
		 String unescapeHtmlUnicode = "";
		 if(StringUtil.isEmptyString(str) || !str.contains("&#")){
			 return str;
		 }	 
		 while(str.contains("&#")){
			 int startIndex = str.indexOf("&#");
			 int endIndex = str.indexOf(";");
			 boolean unescapeHtmlUnicodeFlag = true;
			 if(endIndex == -1){
				 endIndex = str.length()-1;
			 }else if(endIndex < startIndex){
				 startIndex = 0;
				 unescapeHtmlUnicodeFlag = false;
			 }
			 
			 String unescapeHtml = str.substring(startIndex, endIndex+1);
			 if(unescapeHtmlUnicodeFlag && !(unescapeHtml.contains(" ")) ){
				 unescapeHtmlUnicode = unescapeHtmlUnicode + str.substring(0,startIndex) + StringEscapeUtils.unescapeHtml(unescapeHtml);
				 str = str.substring(endIndex+1,str.length());
			 }else{
				 unescapeHtmlUnicode = unescapeHtmlUnicode + str.substring(0,endIndex+1);
				 str = str.substring(endIndex+1,str.length());
			 }
		 }
		 return unescapeHtmlUnicode;
	 }
   
   /**
    * remove first and last char space in string
    * 
    * @param str
    * 
    * @return str
    */
   public static String removeFirstAndLastSpaceInString(String str){
	   str = StringUtils.stripStart(str ,null);
	   str = StringUtils.stripEnd(str, null);
	   return str;
   }
   
   /**
	 * get md5 encrypted string
	 * 
	 * @param source
	 * @return
	 */
	public static String getMD5String(String source){
		MessageDigest md;
		StringBuffer encrypted = new StringBuffer();
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
		    byte byteData[] = md.digest();
		    for (int i = 0; i < byteData.length; i++)
		    	encrypted.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		} catch (NoSuchAlgorithmException e) {
			throw new EazyBpmException(e.getMessage(),e);
		}
		return encrypted.toString();
	}
}
