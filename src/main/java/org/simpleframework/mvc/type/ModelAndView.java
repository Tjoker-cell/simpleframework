package org.simpleframework.mvc.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: simpleframework
 * @description:存储处理完成后的结果数据，以及显示改数据的视图、
 * @author: 十字街头的守候
 * @create: 2021-01-27 11:52
 **/
public class ModelAndView {
    //页面所在的路径
    @Getter
    private String view;
    //页面的data数据
    @Getter
    private Map<String,Object> model=new HashMap<>();
    public ModelAndView setView(String view){
        this.view=view;
        return this;
    }
//    调用链
    public ModelAndView addViewData(String attributeName,Object attributeValue){
        model.put(attributeName,attributeValue);
        return this;
    }
}
