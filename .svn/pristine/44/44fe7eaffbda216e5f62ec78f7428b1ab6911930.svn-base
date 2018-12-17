package com.eazytec.util;

import java.util.List;

/**
 * Created by min on 2016/6/12.
 */
public class PageBean<T> {

	private int totalrecords = 0; // 记录总数

	private int pageSize = 20; // 每页显示记录数

	private int totalpages = 0; // 总页数

	private int currpage = 1; // 当前页数

	private List<T> result;// 结果集

	public int getTotalrecords() {
		return totalrecords;
	}

	public void setTotalrecords(int totalrecords) {
		this.totalrecords = totalrecords;

		if (totalrecords == 0) {
			this.currpage = 0;
			this.totalpages = 0;
		} else {
			if (this.totalrecords % this.pageSize == 0) {
				this.totalpages = this.totalrecords / this.pageSize;
			} else {
				this.totalpages = (this.totalrecords / this.pageSize) + 1;
			}
		}

	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalpages() {

		return totalpages;
	}

	public int getCurrpage() {
		return currpage;
	}

	public void setCurrpage(int currpage) {
		if (currpage < 1) {
			currpage = 1;
		}
		this.currpage = currpage;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public int getStartRow() {
		return (currpage - 1) * pageSize;
	}

	public PageBean(int currpage, int pageSize) {

		if (currpage <= 0) {
			currpage = 0;
		}
		if (pageSize <= 0 || pageSize > 50) {
			pageSize = 15;
		}
		this.currpage = currpage;
		this.pageSize = pageSize;
	}

	public PageBean() {
	}


}
