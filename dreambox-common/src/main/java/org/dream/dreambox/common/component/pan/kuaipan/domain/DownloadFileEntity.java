package org.dream.dreambox.common.component.pan.kuaipan.domain;

public class DownloadFileEntity {

    private AuthEntity auth;
    
    private String root;
    
    private String path;
    
    private String rev;

    public AuthEntity getAuth() {
        return auth;
    }

    public void setAuth(AuthEntity auth) {
        this.auth = auth;
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

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }
    
}
