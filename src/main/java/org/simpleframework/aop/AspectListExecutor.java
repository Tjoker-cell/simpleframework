package org.simpleframework.aop;

import lombok.Getter;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @program: simpleframework
 * @description: 像被代理类中添加横切逻辑
 * @author: 十字街头的守候
 * @create: 2021-01-24 11:31
 **/
public class AspectListExecutor implements MethodInterceptor {
    //被代理的类
    private Class<?> targetClass;
    @Getter
    private List<AspectInfo> aspectInfoList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        aspectInfoList = sortedAspectInfoList(aspectInfoList);
        this.aspectInfoList = aspectInfoList;
    }
    /**
     * @Description:按照order的值进行升序排序，确保order值小的aspect先织入
     * @param aspectInfoList:
     * @return: void
     */
       private List<AspectInfo> sortedAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex()-o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
       Object returnValue=null;
        collectAccurateMatchedAspectList(method);
       if(ValidationUtil.isEmpty(aspectInfoList)){
         returnValue=  methodProxy.invokeSuper(proxy,args);
           return returnValue;
       }
        //1、按照orderd的顺序升序执行完所有aspect的before方法
        invokeBeforeAdvices(method,args);
       try {
            //2、执行被代理类的方法(用代理对象和被代理方法参数)
             methodProxy.invokeSuper(proxy,args);
           //3、如果被代理方法正常返回，则按照order的降序执行完所有asoect中的afterReturning方法
          returnValue = invokeAfterReturningAdvices(method,args,returnValue);
       }catch (Exception e){
           //4、如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvices(method,args,e);
       }
           return returnValue;
    }

    private void collectAccurateMatchedAspectList(Method method) {
        if(ValidationUtil.isEmpty(aspectInfoList)){return ;}
        Iterator<AspectInfo> it=aspectInfoList.iterator();
        while(it.hasNext()){
            AspectInfo aspectInfo=it.next();
            if(!aspectInfo.getPointcutLocator().accurateMatches(method)){
                it.remove();
            }
        }
    }

    //1、按照orderd的顺序升序执行完所有aspect的before方法
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
           for(AspectInfo aspectInfo:aspectInfoList){
               aspectInfo.getAspectObject().before(targetClass,method,args);
           }
    }
    //3、如果被代理方法正常返回，则按照order的降序执行完所有asoect中的afterReturning方法
    @SneakyThrows
    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
       Object result=null;
        for(int i= aspectInfoList.size()-1;i>=0;i--){
           return result = aspectInfoList.get(i).getAspectObject().afterReturning(targetClass,method,args,returnValue);
        }
        return result;
       }
    //4、如果被代理方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for(int i= aspectInfoList.size()-1;i>=0;i--){
          aspectInfoList.get(i).getAspectObject().afterThrowing(targetClass,method,args,e);
        }
    }

}
