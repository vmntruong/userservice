package com.vmnt.userservice.logging;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * AOP logging advice to log input and output for all methods in controller
 * 
 * @author vmntruong
 *
 */
@Aspect
@Component
public class LoggingAdvice {
	
	private final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);
	
	private final ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
			   .addModule(new ParameterNamesModule())
			   .addModule(new Jdk8Module())
			   .addModule(new JavaTimeModule())
			   .build();
	
	@Pointcut(value="execution(* com.vmnt.userservice.controller.UserController.*(..) )")
	public void userControllerPointCut() { }
	
	@Pointcut(value="execution(* com.vmnt.userservice.controller.*.*(..) )")
	public void userControllerExHandlerPointCut() { }
	
	@Around("userControllerPointCut()")
	public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
		
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().toString();
		Object args = pjp.getArgs()[0];

		logger.info("method invoked " + className + " : " + methodName + "() " + "arguments : "
				+ mapper.writeValueAsString(args));
		
		long time = System.currentTimeMillis();
		
		// Get response
		Object object = pjp.proceed();
		
		time = System.currentTimeMillis() - time;
		
		logger.info("{} : {}() Response : {}", className, methodName, mapper.writeValueAsString(object));
		logger.info("{} : {}() Response time : {}", className, methodName, time);
		return object;
	}
	
	@AfterThrowing(pointcut = "userControllerExHandlerPointCut()", throwing = "e")
	public void logError(JoinPoint joinPoint, Throwable e) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();

        logger.info("{} : {}() Exception : {} ms", className, methodName, e.getMessage());
	}
}
