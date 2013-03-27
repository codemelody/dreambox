package org.dream.dreambox.common.component.pan.kuaipan.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class UploadFileRes {

    /** http状态值 */
    private String msg;
    
    /** file_id Y   string  文件唯一标识id */
    @JsonProperty(value="file_id")
    private String fileId;
    
    /** type    Y   enum(file,folder)   folder为文件夹，file为文件 */
    private String type;
    
    /** rev Y   string  文件版本 */
    private String rev;
    
    /** size    Y   uint32  文件大小 */
    private String size;
    
    /** create_time N   string  YYYY-MM-DD hh:mm:ss */
    @JsonProperty(value="create_time")
    private String createTime;
    
    /** modify_time N   string  YYYY-MM-DD hh:mm:ss */
    @JsonProperty(value="modify_time")
    private String modifyTime;
    
    /** name    N   string  文件名 */
    private String name;
    
    /** is_deleted  N   string/JSON bool    是否被删除的文件 */
    @JsonProperty(value="is_deleted")
    private String isDeleted;
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRev() {
        return rev;
    }
    public void setRev(String rev) {
        this.rev = rev;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
    
}
