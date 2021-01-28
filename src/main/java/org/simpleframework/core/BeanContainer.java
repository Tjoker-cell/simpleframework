package org.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.core.annotation.Component;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.core.annotation.Repository;
import org.simpleframework.core.annotation.Service;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: simpleframework
 * @description: bean容器
 * @author: 十字街头的守候
 * @create: 2021-01-18 18:25
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {
    /**
    * 存放所有被配置标记的目标对象的Map
    */
    private final Map<Class<?>,Object> beanMap = new ConcurrentHashMap<>();
    /**
     * 加载bean的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION= Arrays.asList(Component.class, Service.class, Repository.class, Controller.class, Aspect.class);
    /**
     * 容器是否已经加载过
     */
    private boolean loaded=false;
    /** 
    * @Description: 是否被加载过
    * @Param: []
    * @return: boolean
    * @Date: 2021/1/18
    */
    public boolean isLoaded(){
        return loaded;
    }
    /** 
    * @Description: 获取bean容器实例 
    * @Param: []
    * @return: org.simpleframework.core.BeanContainer
    * @Date: 2021/1/18
    */
    public static BeanContainer getInstance(){
        return ContainerHolder.HOLDER.instance;
    }
    /**
    * @Description: 获取bean实例的数量
    * @Param: []
    * @return: int
    * @Date: 2021/1/19
    */
    public int size() {
        return beanMap.size();
    }
    /**
     * @Description:使用内部枚举类单列模式模式创建bean容器对象
     * @return: null
     */
    private enum ContainerHolder{
        HOLDER;
        private BeanContainer instance;
        ContainerHolder(){
            instance=new BeanContainer();
        }
    }

    /**
    * @Description: 扫描加载所有的bean
    * @Param: [packageName]
    * @return: void
    * @Date: 2021/1/18
    */
    public synchronized void loadBeans(String packageName){
        //判断bean是否被加载过
        if(isLoaded()){
            log.warn("bean has been loaded");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        //类是否为空
        if(ValidationUtil.isEmpty(classSet)){
            log.warn("extract nothing from packageName"+packageName);
            return;
        }
        for(Class<?> clazz:classSet){
            for(Class<? extends Annotation> annotation:BEAN_ANNOTATION){
                //如果类上面标记了定义的注解
                if(clazz.isAnnotationPresent(annotation)){
                    //将目标类身作为键，目标类的实例作为值，放入到beanMap中
                    beanMap.put(clazz,ClassUtil.newInstance(clazz,true));

                }
            }
        }
        loaded=true;
    }
    /**
    * @Description: 添加一个class对象及其bean实例
    * @Param: [clazz, bean]
    * @return: java.lang.Object
    * @Date: 2021/1/20
    */
    public Object addBean(Class<?> clazz,Object bean){
        return beanMap.put(clazz, bean);
    }
    /** 
    * @Description: 移除一个IOC容器管理的对象 
    * @Param: [clazz]
    * @return: java.lang.Object
    * @Date: 2021/1/20
    */
    public Object removeBean(Class<?> clazz){
        return beanMap.remove(clazz);
    }
    /**
    * @Description: 根据class对象获取bean实例
    * @Param: [clazz]
    * @return: java.lang.Object
    * @Date: 2021/1/20
    */
   public Object getBean(Class<?> clazz){
        return beanMap.get(clazz);
    }
    /** 
    * @Description: 获取容器中所有的class对象
    * @Param: []
    * @return: java.util.Set<java.lang.Class<?>>
    * @Date: 2021/1/20
    */
    public Set<Class<?>> getClasses(){
       return beanMap.keySet();
     }
     /**
     * @Description: 获取所有的bean集合
     * @Param: []
     * @return: java.lang.Object
     * @Date: 2021/1/20
     */
     public Set<Object> getBeans(){
        return new HashSet<>(beanMap.values());
     }
     /**
     * @Description: 根据注解筛选出bean的class集合
     * @Param: [annotation]
     * @return: java.util.Set<java.lang.Class<?>>
     * @Date: 2021/1/20
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation){
         //1.获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if(ValidationUtil.isEmpty(keySet)){
            log.warn("nothing in beanMap");
            return null;
        }
        //2.通过注解筛选被注解标记的class对象，并添加到classSet里
        Set<Class<?>> classSet=new HashSet<>();
        for(Class<?> clazz:keySet){
            //类是否被相应注解标记
            if(clazz.isAnnotationPresent(annotation)){
                classSet.add(clazz);
            }
        }
        return classSet.size()>0?classSet:null;
     }
    /**
     * @Description:通过接口或者父类获取实现类或者子类的class集合，不包括其本身
     * @Param: [annotation]
     * @return: java.util.Set<java.lang.Class<?>>
     * @Date: 2021/1/20
     */
    public Set<Class<?>> getClassesBySupper(Class<?> interfaceOrClass){
        //1.获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if(ValidationUtil.isEmpty(keySet)){
            log.warn("nothing in beanMap");
            return null;
        }
        //2.通过判断keySet里的元素是否传入的接口或者父类的子类，如果是，将其并添加到classSet里
        Set<Class<?>> classSet=new HashSet<>();
        for(Class<?> clazz:keySet){
            //判断keySet里的元素是否是传入的接口或者类的子类
            if(interfaceOrClass.isAssignableFrom(clazz)&&!clazz.equals(interfaceOrClass)){
                classSet.add(clazz);
            }
        }
        return classSet.size()>0?classSet:null;
    }
}
