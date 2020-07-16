package com.costtime.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Order(1)
public class CostTimeAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.costtime.demo.controller.*.*(..))")
    private void pointcut() {}

   //用around得到方法使用的时间
    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("------costtime aop around begin");
        long begin = System.nanoTime();
        Object obj=joinPoint.proceed();
        long end =System.nanoTime();
        long timeMicro = (end-begin)/1000;
        System.out.println("costtime aop 方法around:微秒数:"+timeMicro);
        System.out.println("------costtime aop around end");
        return obj;
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        System.out.println("------costtime aop doBefore begin");
        startTime.set(System.currentTimeMillis());
    }

    //和doBefore搭配，得到使用的时间
    @AfterReturning(returning = "ret" , pointcut = "pointcut()")
    public void doAfterReturning(Object ret){
        System.out.println("------costtime aop doAfterReturning begin");
        System.out.println("costtime aop 方法doafterreturning:毫秒数:"+ (System.currentTimeMillis() - startTime.get()));
    }

}
