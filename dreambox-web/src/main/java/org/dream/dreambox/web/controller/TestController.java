package org.dream.dreambox.web.controller;

import java.io.File;

import org.dream.dreambox.common.base.BaseController;
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
        RequestTokenRes msg = KuaiPanOAuth.requestToken();
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
        AccessTokenRes a = KuaiPanOAuth.accessToken(r, verifer);
        mav.addObject("token", a.getOauthToken());
        mav.addObject("secret", a.getOauthTokenSecret());
        mav.setViewName("test");
        return mav;
    }
    
    @RequestMapping(value="upload")
    public ModelAndView upload(String secret, String token){
        ModelAndView mav = new ModelAndView();
        RequestTokenRes r1 = new RequestTokenRes();
        r1.setOauthToken(token);
        r1.setOauthTokenSecret(secret);
        UploadLocateRes r = KuaiPanFileUpload.uploadLocate(r1);
        KuaiPanFileUpload.uploadFile(new File("f:/a.txt"), r1, r.getUrl());
        mav.setViewName("test");
        return mav;
    }
}
