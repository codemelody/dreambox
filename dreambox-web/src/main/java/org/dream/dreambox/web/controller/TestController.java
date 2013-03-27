package org.dream.dreambox.web.controller;

import org.dream.dreambox.common.base.BaseController;
import org.dream.dreambox.common.component.pan.kuaipan.KuaiPanOAuth;
import org.dream.dreambox.common.component.pan.kuaipan.domain.RequestTokenRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController extends BaseController {

    @RequestMapping(value="buy-now")
    public ModelAndView test(){
        ModelAndView mav = new ModelAndView();
        //RequestTokenRes msg = KuaiPanOAuth.requestToken();
        String msg = KuaiPanOAuth.authorize(KuaiPanOAuth.requestToken());
        mav.addObject("msg", msg);
        mav.setViewName("test");
        return mav;
    }
    
    @RequestMapping(value="go")
    public ModelAndView getParam(String oauth_token, String oauth_verifier){
        ModelAndView mav = new ModelAndView();
        mav.addObject("oauth_token", oauth_token);
        mav.addObject("oauth_verifier", oauth_verifier);
        mav.setViewName("test");
        return mav;
    }
}
