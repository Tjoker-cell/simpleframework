package org.simpleframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @program: simpleframework
 * @description: 定义框架支持的advice
 * @author: 十字街头的守候
 * @create: 2021-01-24 11:04
 **/
public abstract class DefaultAspect {
    /**
    * @Description: 事前拦截
    * @Param: targetClass 被代理的目标类
    * @param method 被代理的目标方法
    * @param args 被代理目标方法对应的参数列表
    * @return:
    * @Date: 2021/1/24
    */
   public void before(Class<?> targetClass, Method method, Object[] args)throws Throwable{
   }
    /**
     * @Description:事后拦截
     * @param targetClass: 被代理的目标类
     * @param method: 被代理的目标方法
     * @param arge: 被代理的目标方法对应的参数列表
     * @param returnValue: 被代理的目标方法执行后返回值
     * @return: java.lang.Object
     */
    public Object afterReturning(Class<?> targetClass,Method method,Object[] args,Object returnValue)throws Throwable{
       return returnValue;
    }
    /**
     * @Description:
     * @param targetClass: 被代理的目标类
     * @param method: 被代理的目标方法
     * @param arge: 被代理的目标方法对应的参数列表
     * @param e: 被代理的目标方法抛出的异常
     * @return: void
     */
    public void afterThrowing(Class<?> targetClass,Method method,Object[] args,Throwable e)throws Throwable{
        
    }
}
