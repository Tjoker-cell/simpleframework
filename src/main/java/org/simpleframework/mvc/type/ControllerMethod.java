package org.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @program: simpleframework
 * @description: 带执行的Controller及其方法实例和参数的映射
 * @author: 十字街头的守候
 * @create: 2021-01-26 15:05
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    //Controller对应的class对象
    private Class<?> controllerClass;
    //执行的Controller方法实例
    private Method invokeMethod;
    //方法参数名称以及对应的参数类型
    private Map<String,Class<?>> methodParameters;
}
