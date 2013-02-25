package org.dream.dreambox.common.page;

public class PageIndex {
    /** 开始索引*/
    private long startindex;
    /** 结束索引*/
    private long endindex;
    
    public PageIndex(long startindex, long endindex) {  
        this.startindex = startindex;  
        this.endindex = endindex;  
    }  
  
    public long getStartindex() {  
        return startindex;  
    }  
  
    public void setStartindex(long startindex) {  
        this.startindex = startindex;  
    }  
  
    public long getEndindex() {  
        return endindex;  
    }  
  
    public void setEndindex(long endindex) {  
        this.endindex = endindex;  
    }      
  
  
    /** 
     * 算出页码的开始索引和结束索引 
     * @param viewpagecount 页码数量 
     * @param currentPage 当前页数 
     * @param totalpage 总页数 
     * @return 
     */  
  
    public static PageIndex getPageIndex(long viewpagecount, int currentPageNo, long totalPageCount){
        long startpage = currentPageNo - (viewpagecount % 2 == 0 ? viewpagecount / 2 - 1 : viewpagecount / 2);  
        long endpage = currentPageNo + viewpagecount / 2;  
        if(startpage < 1){  
            startpage = 1;  
            if(totalPageCount >= viewpagecount) endpage = viewpagecount;  
            else endpage = totalPageCount;  
        }  
        
        if(endpage == 0){
            endpage =(long)1;
            return new PageIndex(startpage, endpage);
        }
  
        if(endpage > totalPageCount){  
            endpage = totalPageCount;  
            if((endpage - viewpagecount)> 0) startpage = endpage - viewpagecount + 1;  
            else startpage = 1;  
        }  
  
  
        return new PageIndex(startpage, endpage);  
    }
    
}
