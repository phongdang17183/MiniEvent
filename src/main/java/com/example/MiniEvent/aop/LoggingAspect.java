package com.example.MiniEvent.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Before("execution(* com.example.MiniEvent.service..*(..))")
    public void logBeforeServiceCall(JoinPoint joinPoint) {
        log.info("Calling service method {}", joinPoint.getSignature().getName());
    }

    @Before("execution(* com.example.MiniEvent.controller..*(..))")
    public void logBeforeControllerCall(JoinPoint joinPoint) {
        log.info("Calling controller method {}", joinPoint.getSignature().getName());
    }
}
