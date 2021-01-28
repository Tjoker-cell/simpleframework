package org.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: simpleframework
 * @description: 存储http请求路径和请求方法
 * @author: 十字街头的守候
 * @create: 2021-01-26 15:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPathInfo {
    //http请求方法
    private String httpMethod;
    //http请求路径
    private String httpPath;
}
