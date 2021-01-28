package org.simpleframework.inject;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @program: simpleframework
 * @description: 依赖注入
 * @author: 十字街头的守候
 * @create: 2021-01-20 15:26
 **/
@Slf4j
public class DependencyInjector {
    private BeanContainer beanContainer;
    public DependencyInjector() {
       beanContainer = BeanContainer.getInstance();
    }

    public void doIoc(){
        //1.遍历Bean容器中所有的Class对象
        if(ValidationUtil.isEmpty(beanContainer.getClasses())){
            log.warn("empty classset in BeanContainer");
            return;
        }
        for(Class<?> clazz:beanContainer.getClasses()){
            //2.遍历Class对象中所有的成员变量
            Field[] fields = clazz.getDeclaredFields();
            if(ValidationUtil.isEmpty(fields)){
                //跳出当前循环
                  continue;
            }
            for(Field field: fields){
                //3.找出被Autowired标记的成员变量
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    //4.获取这些成员变量的类型
                    Class<?> fieldClass = field.getType();
                    //5.获取这些成员变量的类型在容器里对应的实例
                  Object fieldValue = getFieldInstanct(fieldClass,autowiredValue);
                  if(fieldValue==null){
                      throw new RuntimeException("unable to inject relevant type, target fieldClass is:"+fieldClass.getName()+"autowriedValue:"+autowiredValue);
                  }else{
                      //6.通过反射将对应的成员变量实例注入到成员变量所在的类的实例里面
                      Object targetBean = beanContainer.getBean(clazz);
                      ClassUtil.setField(field,targetBean,fieldValue,true);
                  }

                }
            }




        }


    }
    /**
    * @Description:根据Class在beanContainer里面获取其实例或者实现类
    * @Param: [fieldClass]
    * @return: java.lang.Object
    * @Date: 2021/1/20
    */
    private Object getFieldInstanct(Class<?> fieldClass,String autowriedValue) {
        Object fieldValue=beanContainer.getBean(fieldClass);
        if(fieldValue!=null){
            return fieldValue;
        }else{
           Class<?> implementedClass= getImplementClass(fieldClass,autowriedValue);
           if(implementedClass!=null){
               return beanContainer.getBean(implementedClass);
           }else {
               return null;
           }
        }
    }
    /**
    * @Description: 获取接口的实现类
    * @Param: [fieldClass, autowriedValue]
    * @return: java.lang.Class<?>
    * @Date: 2021/1/20
    */
    private Class<?> getImplementClass(Class<?> fieldClass,String autowriedValue) {
        Set<Class<?>> classSet = beanContainer.getClassesBySupper(fieldClass);
        if(!ValidationUtil.isEmpty(classSet)){
            if(ValidationUtil.isEmpty(autowriedValue)){
                if(classSet.size()==1){
                    return classSet.iterator().next();
                }else{
                    //如果多与两个实现类且用户未指定其中一个实现类，则抛出异常
                    throw new RuntimeException("multiple implemented classes for"+fieldClass.getName()+"pleanse set @Autowired's value to pick one ");
                }
            }else{
                for(Class<?> clazz:classSet){
                    if(autowriedValue.equals(clazz.getSimpleName())){
                        return clazz;
                    }
                }
            }

        }
        return null;
    }
}
