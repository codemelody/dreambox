package org.dream.dreambox.common.component.pan.kuaipan.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FileInfoRes {

    private List<FileInfo> files;
    
    private String path;
    
    private String root;
    
    private String hash;
    
    @JsonProperty(value="file_id")
    private String fileId;
    
    private String type;
    
    @JsonProperty(value="create_time")
    private String createTime;
    
    @JsonProperty(value="modify_time")
    private String modifyTime;
    
    private String name;
    
    private String rev;
    
    @JsonProperty(value="is_deleted")
    private String isDeleted;

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
