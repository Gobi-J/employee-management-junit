package com.i2i.ems.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Aop {

  private static final Logger logger = LogManager.getLogger(Aop.class);

  @Pointcut("execution(* com.i2i.ems.service.*.*(..))")
  public void allServiceMethods() {
  }

  @Around("allServiceMethods()")
  public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
    logger.debug("{} invoked", joinPoint.getSignature());

    Object result = joinPoint.proceed();

    logger.debug("{} completed", joinPoint.getSignature());
    return result;
  }
}