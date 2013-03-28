package org.dream.dreambox.common.component.pan.kuaipan.domain;

public class FileInfoEntity {

    private AuthEntity auth;
    
    /** list    N   string/JSON bool    默认true。
     * 当路径指向是文件夹时，会返回其包含的子文件
     */
    private String list; 
    
    /**
     * file_limit  N   int 默认并且最大为10000，
     * 在查询文件夹信息时，如果文件夹下的文件数目超过 file_limit 时，
     * 不返回文件列表，而是返回 406 (too many files)
     */
    private String fileLimit;
    
    /**
     * page    N   int 默认0 - 不分页；
     * 和 page_size 一起使用；从 1 开始，表示显示第N页的文件
     */
    private String page;
    
    /**
     * page_size   N   int 默认20；
     * page 不等于 0才有效；表示每页显示的文件内容
     */
    private String pageSize;
    
    /**
     * filter_ext  N   string  
     * 过滤特定扩展名的文件，用英文半角逗号分隔，
     * 只能是ascii字符，每一项的长度不超过5，
     * 如：jpg,bmp,png,jpeg 
     * 会返回包含上述扩展的文档（如1.jpg）。
     * 文件夹不会被过滤。请用小写。
     */
    private String filterExt;
    
    /**
     * sort_by N   enum(date, name, size)  默认空，不排序。 
     * 在分页(page!=0)的时候才有效。
     * 排序依据，time - 修改日期 name - 文件名 size - 文件大小 
     * 在前面加上字母"r"的时候代表倒序，
     * 如：sort_by = rsize 表示从大到小排序。
     */
    private String sortBy;

    public AuthEntity getAuth() {
        return auth;
    }

    public void setAuth(AuthEntity auth) {
        this.auth = auth;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getFileLimit() {
        return fileLimit;
    }

    public void setFileLimit(String fileLimit) {
        this.fileLimit = fileLimit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getFilterExt() {
        return filterExt;
    }

    public void setFilterExt(String filterExt) {
        this.filterExt = filterExt;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
}
