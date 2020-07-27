package com.jack.spring.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @author 马钊
 * @date 2020-07-27 09:26
 */
@Aspect
public class MyAspectj {
    @Pointcut("@annotation(com.jack.spring.aop.MyLogAspect)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("before");
    }

    @After("pointCut()")
    public void doAfter() {
        System.out.println("after");
    }

    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void doAfterRetunring(JoinPoint joinPoint, Object result) {
        System.out.println("after-returing");
    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        System.out.println("after-throwing");
    }

    @SneakyThrows
    @Around("pointCut()")
    public void doAround(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("around.begin");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("around.end");
    }
}
