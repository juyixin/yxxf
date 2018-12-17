/*
 *========================================
 * File:      QueryParser.java
 * Project:   Radaptive
 *
 * Author:    Babu. K
 * Revision:  1.0
 *----------------------------------------
 * Copyright 2012 Radaptive Inc.
 *========================================
 */

package com.eazytec.bpm.common;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.eazytec.exceptions.EazyBpmException;

public class QueryParser {
	
	String queryString = null;
	
	String queryType = null;
	
	public QueryParser() {
		// TODO Auto-generated constructor stub
	}
	
	public QueryParser(String queryString) throws Exception {
		this.queryString = getReadableQueryString(queryString);
	}
	
	public QueryParser(String queryString, String queryType) throws Exception {
		this.queryString = getReadableQueryString(queryString);
		this.queryType = queryType;
	}
	
	public String getQueryString() throws Exception {
		return this.queryString;
	}
	
	public List<LabelValueBean> getAllSelectColumns() throws Exception {
		List<LabelValueBean> columnNames = new ArrayList<LabelValueBean>();
		int i = 0;
		List<String> columns = getSelectColumns();
		for(String str:columns) {
			columnNames.add(new LabelValueBean(str, i));
			i++;
		}
		return columnNames;
	}
	
	public List<LabelValueBean> getAllSelectColumnsAlice() throws Exception {
		List<String> columns = getSelectColumns();
		List<LabelValueBean> selectColumns = getColumnAliceNames(columns);
		return selectColumns;
	}
	
	public List<LabelValueBean> getAllSelectColumnsWithoutAlice() throws Exception {
		List<String> columns = getSelectColumns();
		List<LabelValueBean> selectColumns = getColumnWithoutAliceNames(columns);
		return selectColumns;
	}
	
	public String getSelectQueryColumnForAlice(String alice) throws Exception {
		List<String> columns = getSelectColumns();
		for(String column:columns){
			if(column.trim().endsWith(alice)) {
				return column.substring(0, column.lastIndexOf(" as "));
			}
		}
		return null;
	}
	
	public String getSelectQueryColumnForColumn(String columnName) throws Exception {
		List<LabelValueBean> columns = getAllSelectColumnsWithoutAlice();
		for(LabelValueBean column:columns){
			if(column.getLabel().trim().endsWith(columnName))
				return column.getLabel();
		}
		return null;
	}
	
	public String getCountQuery() throws Exception {
        String fromContent = getSplittedContent(queryString, " from ");
        if(queryString.contains(" group by ")) {
            String distinctColumn = queryString.substring(queryString.lastIndexOf(" group by ")+" group by ".length());
            String qryString = queryString.replace(fromContent, "select count(distinct "+distinctColumn+") ");
            return qryString.substring(0, qryString.lastIndexOf(" group by "));
        }
        return queryString.replace(fromContent, "select count(*) ");
    }
	
	public String modifyQryColumnForFilter(String queryColumn) throws Exception {
		queryColumn = removeGroupConcatWhereQryCol(queryColumn);
		queryColumn = removeForWhereQryCol(queryColumn, "distinct");
		return queryColumn;
	}
	
	public String removeWhereClause() throws Exception {
		return getSplittedContent(queryString, " where ");
	}
	
	public String getWhereClause() throws Exception {
		String whereContent = getContent(" where ");
		if(whereContent.isEmpty() || !whereContent.contains("where ")) {
			return null;
		}
		return whereContent.substring(whereContent.indexOf("where ")+6);
	}
	
	public String getOrderByClause() throws Exception {
		String orderByContent = getContent(" order by ");
		if(orderByContent.isEmpty() || !orderByContent.contains("order by ")) {
			return null;
		}
		return orderByContent.substring(orderByContent.indexOf("order by ")+9);
	}
	
	public String getGroupByClause() throws Exception {
		String groupByContent = getContent(" group by ");
		if(groupByContent.isEmpty() || !groupByContent.contains("group by ")) {
			return null;
		}
		return groupByContent.substring(groupByContent.indexOf("group by ")+9);
	}
	
	public String appendInWhereClause(String whereCondition) throws Exception {
		StringBuffer qryString = new StringBuffer();
		String orderByContent = getOrderByClause();
		String groupByContent = getGroupByClause();
		qryString.append(getSplittedContent(queryString, " where ").trim());
		qryString.append(" where ");
		
		if(isWhereClause()) {
			qryString.append(getWhereClause());
			qryString.append(" and ");
		}
		qryString.append(whereCondition);
		
		if(groupByContent != null) {
			qryString.append(groupByContent);
		}
		if(orderByContent != null) {
			qryString.append(orderByContent);
		}
		return qryString.toString();
	}
	
	public boolean isWhereClause() throws Exception {
		String whereContent = getContent(" where ");
		if(whereContent.isEmpty() || !whereContent.contains("where ")) {
			return false;
		}
		return true;
	}
	
	private String getContent(String keyword) throws Exception {
		String content = getSplittedContent(queryString, keyword).trim();
		content = queryString.substring(content.length(), queryString.length());
		return content; 
	}
	
	private String removeForWhereQryCol(String queryColumn, String keyword) throws Exception {
		if(queryColumn.contains(keyword)) {
			queryColumn = queryColumn.replace(keyword, "");
		}
		return queryColumn;
	}
	
	private String removeGroupConcatWhereQryCol(String queryColumn) throws Exception {
		if(queryColumn.contains("group_concat")) {
			String gp_con = getSpecificMethod(queryColumn, "group_concat");
			String token = gp_con.substring(gp_con.indexOf("group_concat(")+"group_concat(".length(), 
					gp_con.indexOf(" separator "));
			
			while(queryColumn.contains("group_concat "))
				 queryColumn = queryColumn.replaceAll("group_concat ", "group_concat");
			queryColumn = queryColumn.replace(gp_con, token);
		}
		return queryColumn;
	}
	
	/**
	 * @author babuk
	 * Makes the given queryString as string readable format
	 * @return Readable queryString
	 * @throws RttException
	 */
	private String getReadableQueryString(String queryString) throws Exception {
		queryString = queryString.replaceAll("\t", " ");
		queryString = queryString.replaceAll("\n", " ");
		while(queryString.contains("  "))
			queryString = queryString.replaceAll("  ", " ");
		queryString = queryString.replaceAll("SELECT ", "select ");
		queryString = queryString.replaceAll(" AS ", " as ");
		queryString = queryString.replaceAll(" FROM ", " from ");
		queryString = queryString.replaceAll(" WHERE ", " where ");
		queryString = queryString.replaceAll(" GROUP BY ", " group by ");
		queryString = queryString.replaceAll(" ORDER BY ", " order by ");
		queryString = queryString.replaceAll(" UNION ", " union ");
		queryString = queryString.replaceAll(" UNION ALL ", " union all ");
		
		queryString = queryString.replaceAll(" SEPARATOR ", " separator ");
		
		queryString = queryString.replaceAll("DISTINCT", "distinct");
		queryString = queryString.replaceAll("GROUP_CONCAT", "group_concat");
		queryString = queryString.replaceAll("DATE_FORMAT", "date_format");
		
		return queryString;
	}
	
	private List<String> getSelectColumns() throws Exception {
		List<String> columns = new ArrayList<String>();
		if(queryString.contains(" from ") && queryString.contains("select")){
			String fromContent = getSplittedContent(queryString, " from ");
			if(fromContent.length() > 7){
				fromContent = fromContent.substring(7);
				columns = splitBy(fromContent, ",");
			}
		}
		return columns;
	}
	
	private List<LabelValueBean> getColumnWithoutAliceNames(List<String> columns) throws Exception {
		List<LabelValueBean> columnNames = new ArrayList<LabelValueBean>();
		int i = 0;
		for(String str:columns) {
			List<String> aliceContent = splitBy(str, " as ");
			if(aliceContent.size() == 2)
				columnNames.add(new LabelValueBean(aliceContent.get(0).toString(), i));
			else{
				columnNames.add(new LabelValueBean(str, i));
			}
			i++;	
		}
		return columnNames;
	}
	
	private List<LabelValueBean> getColumnAliceNames(List<String> columns) throws Exception {
		List<LabelValueBean> aliceNames = new ArrayList<LabelValueBean>();
		int i = 0;
		for(String str:columns) {
			List<String> aliceContent = splitBy(str, " as ");
			if(aliceContent.size() == 2)
				aliceNames.add(new LabelValueBean(aliceContent.get(1).toString(), i));
			else{
				if(str.contains("(") || str.contains(",") || str.contains("'") || !str.contains("."))
					aliceNames.add(new LabelValueBean(str, i));
				else 
					aliceNames.add(new LabelValueBean(str.split("\\.")[1], i));
			}
			i++;	
		}
		return aliceNames;
	}
	
	private List<String> splitBy(String content, String splitter) throws Exception {
		List<String> splitted = new ArrayList<String>();
		String[] splittedString = content.split("(?i)"+splitter);
		StringBuffer sb = new StringBuffer(); 
		for(String sptString:splittedString) {
			sb.append(sptString);
			if(isCountMatch(sb.toString(), "(", ")")){
				splitted.add(sb.toString());
				sb = new StringBuffer();
			}else{
				sb.append(splitter);
			}
		}
		return splitted;
	}
	
	/*private List<LabelValueBean> getAliasColumnNames(String fromContent) throws Exception {
		List<LabelValueBean> columns = new ArrayList();
		String[] splitByAS = fromContent.split("(?i) as ");
		int j = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(splitByAS[0].toString());
		boolean isAliceColumn = isCountMatch(sb.toString(), "(", ")");
		for(int i=1;splitByAS.length > i; i++) {
			sb.append(splitByAS[i].toString());
			if(isAliceColumn) {
				columns.add(new LabelValueBean(splitByAS[i].split(",")[0].trim(), j));
				j++;
			}
			isAliceColumn = isCountMatch(sb.toString(), "(", ")");
		}
		return columns;
	}*/
	
	/**
	 * @author babuk
	 * Gets the first field of the select column of the given query
	 * @param queryString
	 * @return first coloumn name from select statement
	 * @throws RttException
	 */
	private String getFirstSelectColumn(String queryString) throws Exception {
		String selectColumn = null;
		String fromContent = getSplittedContent(queryString, " from ");
		String firstContent = getSplittedContent(fromContent, ",");
		selectColumn = firstContent.substring(firstContent.indexOf("select ")+7, firstContent.length());
		return selectColumn;
	}
	
	/**
	 * @author babuk
	 * It will give column name without alice name
	 */
	private String removeAlice(String column) throws Exception {
		return column.substring(0, column.lastIndexOf(" as "));
	}

	/**
	 * @author babuk
	 * This method used to get splitted content of query.
	 * ie. If string array has been splitted by keyword 'from',
	 * we get the string content which has exact query upto the keyword 'from'.
	 * Even though any subquery is their in the query, we will get the exact result. 
	 * @param splittedString
	 * @param opener
	 * @param closer
	 * @return part of the query content
	 * @throws RttException
	 */
	private String getSplittedContent(String queryString, String splitter) throws Exception {
		String[] splittedString = queryString.split("(?i)"+splitter);
		String fromContent = null;
		StringBuffer sb = new StringBuffer(); 
		for(String sptString:splittedString) {
			sb.append(sptString);
			if(isCountMatch(sb.toString(), "(", ")")) {
				fromContent = sb.toString();
				return fromContent;
			}else{
				sb.append(splitter);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author babuk
	 * True will be return only count matches of the opener and closer in given content
	 * @param content
	 * @param opener
	 * @param closer
	 * @return
	 * @throws RttException
	 */
	private boolean isCountMatch(String content, String opener, String closer) throws Exception {
		if( StringUtils.countMatches(content, opener) == StringUtils.countMatches(content, closer) ) {
			return true;
		}
		return false;
	}

	private String getSpecificMethod(String queryString, String method) throws Exception {
		String methodContent = queryString.substring(queryString.indexOf(method)+method.length());
		char[] methodArray = methodContent.substring(methodContent.indexOf("(")).toCharArray();
		
		String fromContent = null;
		StringBuffer sb = new StringBuffer(); 
		for(char sptString:methodArray) {
			sb.append(sptString);
			if(isCountMatch(sb.toString(), "(", ")")) {
				fromContent = sb.toString();
				return method+fromContent;
			}
		}
		return sb.toString();
	}
}
