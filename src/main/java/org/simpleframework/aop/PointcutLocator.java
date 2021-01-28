package org.simpleframework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * @program: simpleframework
 * @description: 解析Aspect表示式并且定位被织入的目标
 * @author: 十字街头的守候
 * @create: 2021-01-25 09:54
 **/
public class PointcutLocator {
    /**
     *Pointcut解析器,直接给他赋值上AspectJ的所有表达式,以便支持对众多表达式的解析
     */
    private PointcutParser pointcutParser=PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
          PointcutParser.getAllSupportedPointcutPrimitives()
    );
    /**
     *表达式解析器
     */
    private PointcutExpression pointcutExpression;
    public PointcutLocator(String expression){
        this.pointcutExpression=pointcutParser.parsePointcutExpression(expression);
    }
    /**
     * @Description:判断传入的class对象是否是Aspect的目标代理类，即匹配Pointcut表达式（初筛）
     * @param targetClass: 目标类
     * @return: boolean
     */
    public boolean roughMatches(Class<?> targetClass){
        //couldMatchJoinPointsInType比较坑，只能效验within
        //不能效验（execution,call,get,set），面对无法效验的表达式，直接返回true
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }
    /**
     * @Description:判断传入的Method对象是否是Aspect的目标dialing方法，即匹配Pointcut表达式（精筛选）
     * @param method:
     * @return: boolean
     */
    public boolean accurateMatches(Method method){
        ShadowMatch shadowMatch = pointcutExpression.matchesAdviceExecution(method);
        if(shadowMatch.alwaysMatches()){
            return true;
        }else {
            return false;
        }

    }
}
