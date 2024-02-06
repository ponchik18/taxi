package com.modsen.promocodeservice.advice

import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAdvice(private val mapper: ObjectMapper) {

    private val log: Logger = LoggerFactory.getLogger(LoggingAdvice::class.java)

    @Around("myPointcut()")
    fun applicationLogger(joinPoint: ProceedingJoinPoint): Any {
        val methodName = joinPoint.signature.name
        val className = joinPoint.target.javaClass.toString()
        val arrayOfArgument = joinPoint.args

        log.info("method invoke {}: {}() arguments :{}", className, methodName, mapper.writeValueAsString(arrayOfArgument))
        val result = joinPoint.proceed()
        log.info("{}: {}() Response", className, methodName)
        return result
    }

    @Pointcut("execution(* com.modsen.promocodeservice.*.*.*(..)) && !within(com.modsen.promocodeservice.config..*)")
    fun myPointcut() {
    }
}