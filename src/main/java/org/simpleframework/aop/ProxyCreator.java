package org.simpleframework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @program: simpleframework
 * @description: 创建代理对象
 * @author: 十字街头的守候
 * @create: 2021-01-24 15:19
 **/
public class ProxyCreator {
    /**
     * @Description:创建动态代理对象并返回
     * @param targetClass: 被代理的class对象
     * @param methodInterceptor: 方法拦截器
     * @return: java.lang.Object
     */
    public static Object createProxy(Class<?> targetClass, MethodInterceptor methodInterceptor){
        return Enhancer.create(targetClass,methodInterceptor);
    }
}
