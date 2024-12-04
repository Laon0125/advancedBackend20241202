package com.lion.demo.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Aspect
@Component
public class ExecutionTimeAspect {
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 클래스명과 메소드명 가져오기
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        // 시작시간
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("Method: " + className + "." + methodName + "() | Start time: " + startTime);
        Object result = joinPoint.proceed();        // Primary concern 실행
        // 종료시간
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("Method: " + className + "." + methodName + "() | End time: " + endTime);
        // 종료시간 - 시작시간 차이 출력
        long duration = java.time.Duration.between(startTime, endTime).toMillis();
        System.out.println("Method: " + className + "." + methodName + "() | Duration: " + duration + " ms");
        return result;
    }
}