package org.dream.dreambox.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class BaseController extends MultiActionController {
    /**
     * 直接输出纯字符串
     */
    public void renderText(HttpServletResponse response, String content) {
        try {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 直接输出纯HTML
     */
    public void renderHtml(HttpServletResponse response, String content) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(content);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 直接输出纯XML
     */
    public void renderXML(HttpServletResponse response, String content) {
        try {
            response.setContentType("text/xml;charset=UTF-8");
            response.getWriter().write(content);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 直接输出json
     */
    public void renderJson(HttpServletResponse response, String content) {
        try {
            response.setContentType("text/json;charset=UTF-8");
            response.getWriter().write(content);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Convenience method for getting a i18n key's value. Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
     * 
     * @param msgKey
     * @return
     */
    public String getText(String msgKey) {
        return getMessageSourceAccessor().getMessage(msgKey);
    }

    /**
     * Convenient method for getting a i18n key's value with a single string
     * argument.
     * 
     * @param msgKey
     * @param arg
     * @return
     */
    public String getText(String msgKey, String arg) {
        return getText(msgKey, new Object[] { arg });
    }

    /**
     * Convenience method for getting a i18n key's value with arguments.
     * 
     * @param msgKey
     * @param args
     * @return
     */
    public String getText(String msgKey, Object[] args) {
        return getMessageSourceAccessor().getMessage(msgKey, args);
    }
    
    /**
     * 记录日志。
     * @return
     */
    public Logger getLogger(){
        return LoggerFactory.getLogger(BaseController.class);
    }

}
