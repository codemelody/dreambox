package org.dream.dreambox.common.page;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
    public static int DEFAULT_PAGE_SIZE = 20;

    private int pageSize = DEFAULT_PAGE_SIZE; // 每页的记录数

    private long start; // 当前页第一条数据在List中的位置,从0开始

    private List<T> result; // 当前页中存放的记录,类型一般为List

    private long totalCount; // 总记录数

    /** 页码开始索引和结束索引 **/
    private PageIndex pageindex;
    
    public PageIndex getPageindex() {
        return pageindex;
    }

    public void setPageindex(PageIndex pageindex) {
        this.pageindex = pageindex;
    }

    private long totalPageCount;

    private boolean hasNextPage = false;

    private boolean hasPreviousPage = false;
    
    private long currentPageNo = 0;

    /**
     * 构造方法，只构造空页.
     */
    public Page() {
        this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList<T>());
    }

    /**
     * 默认构造方法.
     * 
     * @param start
     *            本页数据在数据库中的起始位置
     * @param totalSize
     *            数据库中总记录条数
     * @param pageSize
     *            本页容量
     * @param data
     *            本页包含的数据
     */
    public Page(long start, long totalSize, int pageSize, List<T> data) {
        this.pageSize = pageSize;
        this.start = start;
        this.totalCount = totalSize;
        this.result = data;
        getTotalPageCount();
        hasNextPage();
        hasPreviousPage();
    }

    /**
     * 取总记录数.
     */
    public long getTotalCount() {
        return this.totalCount;
    }

    /**
     * 取总页数.
     */
    public long getTotalPageCount() {
        if (pageSize > 0) {
            if (totalCount % pageSize == 0) {
                totalPageCount = totalCount / pageSize;
                return totalPageCount;
            } else {
                totalPageCount = totalCount / pageSize + 1;
                return totalPageCount;
            }
        }
        return 1;
    }

    /**
     * 取每页数据容量.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 取当前页中的记录.
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 取该页当前页码,页码从1开始.
     */
    public long getCurrentPageNo() {
        if (pageSize > 0) {
            currentPageNo = start / pageSize + 1;
            return currentPageNo;
        } 
        return 0;
    }

    /**
     * 该页是否有下一页.
     */
    public boolean hasNextPage() {
        hasNextPage = this.getCurrentPageNo() < this.getTotalPageCount();
        return hasNextPage;
    }

    /**
     * 该页是否有上一页.
     */
    public boolean hasPreviousPage() {
        hasPreviousPage = this.getCurrentPageNo() > 1;
        return hasPreviousPage;
    }

    /**
     * 获取任一页第一条数据在数据集的位置，每页条数使用默认值.
     * 
     * @see #getStartOfPage(int,int)
     */
    protected static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 获取任一页第一条数据在数据集的位置.
     * 
     * @param pageNo
     *            从1开始的页号
     * @param pageSize
     *            每页记录条数
     * @return 该页第一条数据
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        if (pageNo > 0) {
            return (pageNo - 1) * pageSize;
        }
        return 0;
    }

}
