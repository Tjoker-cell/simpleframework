package org.simpleframework.mvc;

import org.simpleframework.aop.AspectWeaver;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.DependencyInjector;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.impl.ControllerRequestProcessor;
import org.simpleframework.mvc.processor.impl.JspRequestProcessor;
import org.simpleframework.mvc.processor.impl.PreRequestProcessor;
import org.simpleframework.mvc.processor.impl.StaticResourceRequestProcessor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: simpleframework
 * @description: dispatcherServlet分发器
 * @author: 十字街头的守候
 * @create: 2021-01-16 20:44
 **/
@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
    List<RequestProcessor>  PROCESSOR=new ArrayList<>();

    @Override
    public void init(){
        //1、初始化容器
        BeanContainer beanContainer=BeanContainer.getInstance();
        beanContainer.loadBeans("tjoker");
        new AspectWeaver().doAop();
        new DependencyInjector().doIoc();
        //2、初始化请求处理器责任链
        PROCESSOR.add(new PreRequestProcessor());
        PROCESSOR.add(new StaticResourceRequestProcessor(getServletContext()));
        PROCESSOR.add(new JspRequestProcessor(getServletContext()));
        PROCESSOR.add(new ControllerRequestProcessor());
    }
   @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        //1、创建责任链对象实例
      RequestProcessorChain requestProcessorChain= new RequestProcessorChain(PROCESSOR.iterator(),req,resp);
       //2、通过责任链模式来依次调用请求处理对请求进行处理
        requestProcessorChain.doRequestProcessorChain();
       //3、对处理结果进行渲染
       requestProcessorChain.doRender();
   }


}
