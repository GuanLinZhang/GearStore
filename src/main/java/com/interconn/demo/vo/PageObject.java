package com.interconn.demo.vo;

import lombok.Data;

import java.util.List;

public class PageObject<T> {
	/**具体数据信息*/
	private List<T> records;
	/**总记录数*/
	private Integer rowCount;
	/**总页数*/
	private Integer pageCount;
	/**当前页码*/
	private Integer pageCurrent;


	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(Integer pageCurrent) {
		this.pageCurrent = pageCurrent;
	}
}
