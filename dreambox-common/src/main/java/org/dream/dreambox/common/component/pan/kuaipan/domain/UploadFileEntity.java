package org.dream.dreambox.common.component.pan.kuaipan.domain;

public class UploadFileEntity {

    private AuthEntity auth;
    private String sourceIp;
    private String overwrite;
    private String file;
    private String root;
    private String path;
    public AuthEntity getAuth() {
        return auth;
    }
    public void setAuth(AuthEntity auth) {
        this.auth = auth;
    }
    public String getSourceIp() {
        return sourceIp;
    }
    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
    public String getOverwrite() {
        return overwrite;
    }
    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getRoot() {
        return root;
    }
    public void setRoot(String root) {
        this.root = root;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    
}
