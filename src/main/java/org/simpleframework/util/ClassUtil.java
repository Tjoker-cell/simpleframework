package org.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: simpleframework
 * @description: 类相关通用方法通过类加载器获取资源信息
 * @author: 十字街头的守候
 * @create: 2021-01-18 10:23
 **/
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";
    /** 
    * @Description: 设置类的属性值 
    * @Param: [field, target, value, accessible]
    * @return: void
    * @Date: 2021/1/20
    */
    public static void setField(Field field,Object target,Object value,boolean accessible){
        try {
            field.setAccessible(accessible);
            field.set(target,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取包下类集合
     * @param packageName
     * @return 类集合
     */
  public static Set<Class<?>> extractPackageClass(String packageName){
      //1.获取类的加载器
      ClassLoader classLoader = getClassLoader();
      //2.通过类加载器获取加载的资源
      URL url = classLoader.getResource(packageName.replace(".", "/"));
      if(url==null){
          log.warn("unable to retrieve anything from package:"+packageName);
          return null;
      }
      //3.依据不同的资源类型，采用不同的方式获取资源的集合
      Set<Class<?>> classSet=null;
      //过滤出文件类型的资源
      if(url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)){
          classSet=new HashSet<>();
          File packageDirectory = new File(url.getPath());
          extractClassFile(classSet,packageDirectory,packageName);
      }
      return classSet;
    }
    /** 
    * @Description: 实例化class
    * @Param: [clazz,accessible：是否支持私有class对象的实例]
    * @return: T
    * @Date: 2021/1/18
    */
    public static <T> T newInstance(Class<?> clazz,boolean accessible){
        try {
            Constructor constructor= clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
          return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error",e);
            throw new RuntimeException(e);
        }
    }
    /** 
    * @Description: 递归获取目标package里面的所有class文件（包括子package里的class文件） 
    * @Param: [classSet, packgaeDirectory, packageName]
    * @return: void
    * @Date: 2021/1/18
    */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        if(!fileSource.isDirectory()){
            return;
        }
        //如果是一个文件夹，则调用器listFiles方法获取文件夹下的文件或者文件夹
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.isDirectory()){
                    return true;
                }else{
                    //获取文件的绝对值路径
                    String absoluteFilePath = file.getAbsolutePath();
                    if(absoluteFilePath.endsWith(".class")){
                        //若是class文件则直接加载
                        addToClassSet(absoluteFilePath);
                    }

                }
                return false;
            }
            //根据class文件的绝对路径，获取并生成class对象，并放入classSet中
            private  void addToClassSet(String absoluteFilePath) {
                //1.从class文件的绝对值路径里面提取出包含了package的类名
                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                String className=absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className= className.substring(0,className.lastIndexOf("."));
                //2.通过反射机制获取对应的class对象并加入到classSet里
                Class<?> targerClass = loadClass(className);
                emptyClassSet.add(targerClass);
            }
        });
        if(files!=null){
            for(File f:files){
                //递归调用
                extractClassFile(emptyClassSet,f,packageName);
            }
        }
    }
    /**
    * @Description: 加载class对象
    * @Param: [className]
    * @return: java.lang.Class<?>
    * @Date: 2021/1/18
    */
    private static Class<?> loadClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz;
        } catch (ClassNotFoundException e) {
            log.error("load class error",e);
            throw new RuntimeException(e);
        }
    }


    /**
  * @Description:
  * @Param: [id]
  * @return: java.lang.ClassLoader
  * @Date: 2021/1/18
  */
    public static ClassLoader getClassLoader(){
      return Thread.currentThread().getContextClassLoader();
    }

}
