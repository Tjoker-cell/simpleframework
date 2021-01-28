package org.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.processor.RequestProcessor;

/**
 * @program: simpleframework
 * @description:请求预处理，包括编码以及路径处理
 * @author: 十字街头的守候
 * @create: 2021-01-25 17:50
 **/
@Slf4j
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
      //1、设置请求编码，将统一设置为UTF-8
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");
      //2、将请求路径末尾的/剔除，为后续匹配Controler请求路径做准备
        // （一般Controller的处理路径是/aaa/bbb，所以如果传入的路径结尾是/aaa/bbb/，
        // 就需要处理成/aaa/bbb）
        String requestPath=requestProcessorChain.getRequestPath();
        if(requestPath.length()>1&&requestPath.endsWith("/")){
            requestProcessorChain.setRequestPath(requestPath.substring(0,requestPath.length()-1));
        }
        log.info("preprocess request {} {}", requestProcessorChain.getRequestMethod(), requestProcessorChain.getRequestPath());
        return true;
    }
}
