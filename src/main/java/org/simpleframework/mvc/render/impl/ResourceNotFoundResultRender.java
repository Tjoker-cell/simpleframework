package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: simpleframework
 * @description: 资源找不到时使用的渲染器
 * @author: 十字街头的守候
 * @create: 2021-01-26 10:40
 **/
public class ResourceNotFoundResultRender implements ResultRender {
    private String httpMethod;
    private String httpPath;
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
                "获取不到对应的请求资源：请求路径[" + httpPath + "]" + "请求方法[" + httpMethod + "]");
    }
    public ResourceNotFoundResultRender(String httpMethod,String httpPath) {
        this.httpMethod = httpMethod;
        this.httpPath=httpPath;
    }
}
