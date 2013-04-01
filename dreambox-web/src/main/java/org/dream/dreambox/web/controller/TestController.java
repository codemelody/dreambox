package org.dream.dreambox.web.controller;

import java.io.File;

import org.dream.dreambox.common.base.BaseController;
import org.dream.dreambox.common.component.pan.kuaipan.KuaiPanDownload;
import org.dream.dreambox.common.component.pan.kuaipan.KuaiPanFileInfo;
import org.dream.dreambox.common.component.pan.kuaipan.KuaiPanFileUpload;
import org.dream.dreambox.common.component.pan.kuaipan.KuaiPanOAuth;
import org.dream.dreambox.common.component.pan.kuaipan.domain.AccessTokenRes;
import org.dream.dreambox.common.component.pan.kuaipan.domain.RequestTokenRes;
import org.dream.dreambox.common.component.pan.kuaipan.domain.UploadLocateRes;
import org.dream.dreambox.common.component.pan.kuaipan.util.KuaiPanGlobal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController extends BaseController {

    @RequestMapping(value="test")
    public ModelAndView test(){
        ModelAndView mav = new ModelAndView();
        KuaiPanOAuth service = new KuaiPanOAuth();
        RequestTokenRes msg = service.requestToken();
        mav.addObject("url", KuaiPanGlobal.AUTHORIZE_URL + msg.getOauthToken());
        mav.addObject("token", msg.getOauthToken());
        mav.addObject("secret", msg.getOauthTokenSecret());
        mav.setViewName("test");
        return mav;
    }
    
    @RequestMapping(value="auth")
    public ModelAndView auth(String token, String secret, String verifer){
        ModelAndView mav = new ModelAndView();
        RequestTokenRes r = new RequestTokenRes();
        r.setOauthToken(token);
        r.setOauthTokenSecret(secret);
        KuaiPanOAuth service = new KuaiPanOAuth();
        AccessTokenRes a = service.accessToken(r, verifer);
        mav.addObject("token", a.getOauthToken());
        mav.addObject("secret", a.getOauthTokenSecret());
        mav.setViewName("test");
        return mav;
    }
    
    @RequestMapping(value="upload")
    public ModelAndView upload(String secret, String token){
        ModelAndView mav = new ModelAndView();
        AccessTokenRes r1 = new AccessTokenRes();
        r1.setOauthToken(token);
        r1.setOauthTokenSecret(secret);
        KuaiPanFileUpload service = new KuaiPanFileUpload();
        UploadLocateRes r = service.uploadLocate(r1);
        service.uploadFile(r1, r.getUrl(), new File("f:/a.txt"), "/test.txt");
        mav.addObject("token", r1.getOauthToken());
        mav.addObject("secret", r1.getOauthTokenSecret());
        mav.setViewName("test");
        return mav;
    }
    
    @RequestMapping(value="metadata")
    public ModelAndView metadata(String secret, String token){
        ModelAndView mav = new ModelAndView();
        KuaiPanFileInfo service = new KuaiPanFileInfo();
        AccessTokenRes r1 = new AccessTokenRes();
        r1.setOauthToken(token);
        r1.setOauthTokenSecret(secret);
        service.getMetadata(r1);
        mav.addObject("token", r1.getOauthToken());
        mav.addObject("secret", r1.getOauthTokenSecret());
        mav.setViewName("test");
        return mav;
    }
    
    @RequestMapping(value="download")
    public ModelAndView download(String secret, String token){
        ModelAndView mav = new ModelAndView();
        AccessTokenRes r = new AccessTokenRes();
        r.setOauthToken(token);
        r.setOauthTokenSecret(secret);
        KuaiPanDownload service = new KuaiPanDownload();
        service.downloadFile(r, "f:/test1.txt", "/test.txt");
        mav.addObject("token", r.getOauthToken());
        mav.addObject("secret", r.getOauthTokenSecret());
        mav.setViewName("test");
        return mav;
    }
}
