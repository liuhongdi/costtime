package com.costtime.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Component
@Aspect
@Order(2)
public class LogAspect {

    @Pointcut("execution(public * com.costtime.demo.controller.*.*(..))")
    private void pointcut() {}

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("------log aop around begin");
        Object obj=joinPoint.proceed();
        System.out.println("------log aop around end");
        return obj;
    }

    //把请求参数打印出来
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        System.out.println("------log aop doBefore begin");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //得到方法的参数名和参数值
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        String[] paramNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        String paramValue = "";
        Map<String, Object> nameAndArgs = new HashMap<String, Object>();
        for (int i = 0; i < paramNames.length; i++) {
            paramValue+="parameter:"+paramNames[i]+";value:"+args[i];
        }

        // 记录下请求内容
        System.out.println("URL : " + request.getRequestURL().toString());
        System.out.println("PARAM : " +  request.getQueryString());
        System.out.println("HTTP_METHOD : " + request.getMethod());
        System.out.println("IP : " + request.getRemoteAddr());
        System.out.println("METHOD CLASS : " + joinPoint.getSignature().getDeclaringTypeName() );
        System.out.println("METHOD NAME: " + joinPoint.getSignature().getName());
        System.out.println("METHOD ARGS : " + paramValue);
    }

    @AfterReturning(returning = "ret" , pointcut = "pointcut()")
    public void doAfterReturning(Object ret){
        System.out.println("------log aop doAfterReturning begin");
    }

}
