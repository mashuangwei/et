package com.msw.et.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mashuangwei on 2018/7/15.
 */
@Aspect
@Component
public class HttpAspect {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
    /**
     * 请求开始时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    @Pointcut("execution(* com.msw.et.controller.*.*(..))")
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        logger.info("url={}",httpServletRequest.getRequestURI());
        logger.info("method={}",httpServletRequest.getMethod());
        logger.info("ip={}",httpServletRequest.getRemoteAddr());
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());
        logger.info("args={}",joinPoint.getArgs());
    }

    @Before("log()")
    public void doBeforeInServiceLayer() {
        logger.info("Request Handler Begin");
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void afterReturning(Object object){
        if (null != object){
            logger.info("response={}",object.toString());
        } else {
            logger.info("response=null");
        }

    }

    @AfterThrowing(throwing = "ex", pointcut = "log()")
    public void doAfterThrowing(Throwable ex) {
        logger.info("Spend Time : {}ms", (System.currentTimeMillis() - startTime.get()));
        logger.info("Request Have A Exception", ex);
    }

    @After("log()")
    public void doAfterInServiceLayer() {
        logger.info("Spend Time : {}ms", (System.currentTimeMillis() - startTime.get()));
        logger.info("Request Handler End");
    }


}
