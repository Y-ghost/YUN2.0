package com.rest.yun.dto;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = -9111114856097767106L;

	private int pageNum;
	private int pageSize;
	private int startRow;
	private int endRow;
	private long total;
	private long pages;
	private List<T> result;

	public Page(int pageNum, int pageSize) {
		this.pageNum = pageNum <= 0 ? 1 : pageNum;
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
		this.startRow = (this.pageNum - 1) * this.pageSize;
		this.endRow = pageNum * pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
		this.pages = (this.total % pageSize == 0) ? (this.total / this.pageSize) : (this.total / this.pageSize) + 1;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getPages() {
		return pages;
	}

	public void setPages(long pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [pageNum=");
		builder.append(pageNum);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", startRow=");
		builder.append(startRow);
		builder.append(", endRow=");
		builder.append(endRow);
		builder.append(", total=");
		builder.append(total);
		builder.append(", pages=");
		builder.append(pages);
		builder.append(", result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}

}
