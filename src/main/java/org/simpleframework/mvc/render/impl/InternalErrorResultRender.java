package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: simpleframework
 * @description: 异常渲染器
 * @author: 十字街头的守候
 * @create: 2021-01-26 10:40
 **/
public class InternalErrorResultRender implements ResultRender {
    private String erroMsg;

    public InternalErrorResultRender(String erroMsg) {
        this.erroMsg = erroMsg;
    }
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,erroMsg);
    }
}
