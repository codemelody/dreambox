package org.dream.dreambox.common.component.pan.kuaipan.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class FileInfo {
    
    @JsonProperty(value="file_id")
    private String fileId;
    
    private String type;
    
    private String size;
    
    @JsonProperty(value="create_time")
    private String createTime;
    
    @JsonProperty(value="modify_time")
    private String modifyTime;
    
    private String name;
    
    private String rev;
    
    @JsonProperty(value="is_deleted")
    private String isDeleted;

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

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
    
}
