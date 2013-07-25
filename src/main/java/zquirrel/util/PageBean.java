package zquirrel.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PageBean {
	
	private static Log log = LogFactory.getLog(PageBean.class);

	private int pageNo = 1;
	private int pageSize;
	private int maxPages;
	
	public PageBean() {
	}

	public PageBean(int pageNo, int pageSize, int max) {
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
		this.setMaxRecords(max);
		
		log.debug("PageNo: " + this.pageNo + "; PageSize: " + pageSize + "; PageMax: " + max);
	}

	public int getStart() {
		return (pageNo-1)*pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getMaxPages() {
		return (int) Math.ceil(maxPages / (double) pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxRecords() {
		return maxPages;
	}
	
	public int getNextPage() {
		if (pageNo < getMaxPages()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}
	
	public int getPrevPage() {
		if (pageNo > 1) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	public void setMaxRecords(int maxCount) {
		this.maxPages = maxCount;
	}
	
	public boolean hasNext() {
		return pageNo < getMaxPages();
	}
	
	public boolean hasPrev() {
		return pageNo > 1;
	}

}
