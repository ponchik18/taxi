package com.modsen.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAdvice {
    private static final Logger log = LoggerFactory.getLogger(LoggingAdvice.class);

    private final ObjectMapper mapper;

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        Object[] arrayOfArgument = joinPoint.getArgs();

        log.info("method invoke {}: {}() arguments :{}", className, methodName, mapper.writeValueAsString(arrayOfArgument));
        Object object = joinPoint.proceed();
        log.info("{}: {}() Response", className, methodName);
        return object;
    }

    @Pointcut(value = "execution(* com.modsen.*.*.*(..) ) && !within(com.modsen.config..*)")
    public void myPointcut() {
    }

}