# simpleframework
自研spring框架，实现SpringIoc,SpringAop,SpringMVC
## 自研简易版Spring框架
自研框架实现SpringIoC、SpringAOP、SpringMVC等功能
项目源码已经上传到GitHub，先标星在观看养成好习惯

**必备知识**

 - Spring基础知识
 - 设计模式：单例、工厂、模板方法、代理、责任链等设计模式
 - 注解创建与使用
 - Java反射机制
 - JDK动态代理、CGLIB动态代理
 - lombok插件的使用

 **项目结构**
	 <img src="https://img-blog.csdnimg.cn/20210128163327100.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNTkxODk5,size_20,color_FFFFFF,t_70#pic_left"   width="60%">

**框架环境搭建**

主要搭建框架过程中使用的jar包、配置信息、工具类作为一个总览
以下环境是我开发自研框架所使用的软件环境，环境对于自研框架问题不大，重要的是添加相应的配置信息和工具类
 - Maven3.6.3
 - JDK8
 - IDEA2020.3
 
配置文件信息
pom.xml
```xml
 <dependencies>
    <!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.7.0</version>
      <scope>test</scope>
    </dependency>
    <!-- https://mvnrepository.com/artifact/cglib/cglib -->
    <dependency>
      <groupId>cglib</groupId>
      <artifactId>cglib</artifactId>
      <version>3.2.9</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.3</version>
      <scope>provided</scope>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.9.6</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.8.5</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.3</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.28</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.10</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
  </dependencies>
```
日志配置文件 log4j.proproperties

```xml
### 设置日志的打印级别以及要输出到的地方###
# 优先级从高到低分别是ERROR、WARN、INFO、DEBUG
# 比如在这里定义了INFO级别，则应用程序中所有DEBUG级别的日志信息将不被打印出来
# stdout指代要输出到的地方，可以是不同的自定义名称，也可以有多个，表示输出到多个地方
log4j.rootLogger=debug,stdout

### 输出信息到控制台 ###
### 输出信息到控制台 ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout

### 输出日志的格式 ###
#%c：输出日志信息所属的类目，通常就是所在类的全名
#%d：输出日志时间点的日期或时间
#%p：输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL
#%m：输出代码中指定的消息,产生的日志具体信息
#%n：输出一个回车换行符，Windows  平台为"\r\n"，Unix平台为"\n"输出日志信息换行
#这里的示例：com.imooc.HelloServlet 17:48:00 -- INFO -- test
log4j.appender.stdout.layout.ConversionPattern=%c %d{HH:mm:ss} -- %p -- %m%n
```
ValidationUtil工具类 对对象进行非空判断

```java

public class ValidationUtil {
    /**
     * String是否为null或""
     *
     * @param obj String
     * @return 是否为空
     */
    public static boolean isEmpty(String obj) {
        return (obj == null || "".equals(obj));
    }

    /**
     * Array是否为null或者size为0
     *
     * @param obj Array
     * @return 是否为空
     */
    public static boolean isEmpty(Object[] obj) {
        return obj == null || obj.length == 0;
    }
    /**
     * Collection是否为null或size为0
     *
     * @param obj Collection
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> obj){
        return obj == null || obj.isEmpty();
    }
    /**
     * Map是否为null或size为0
     *
     * @param obj Map
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> obj) {
        return obj == null || obj.isEmpty();
    }
}

```
一切准备就绪，那我们就开始进入到自研框架的阵地
(2) 实现Spring容器和IoC

[自研Spring框架之IoC](https://blog.csdn.net/qq_43591899/article/details/113354553)

(3) 实现Spring AOP和事务管理

[自研Spring框架之AOP](https://blog.csdn.net/qq_43591899/article/details/113382736)

(4) 实现Spring MVC

[自研Spring框架之MVC](https://blog.csdn.net/qq_43591899/article/details/113386122)


