package com.example.profile.service.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    private static final long SLOW_CALL_THRESHOLD_MS = 250L;

    @Around("execution(public * com.example.profile.service.controller..*(..))")
    public Object tracePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        if (elapsedMs >= SLOW_CALL_THRESHOLD_MS) {
            log.warn("Slow request path={} durationMs={}", joinPoint.getSignature().toShortString(), elapsedMs);
        } else if (log.isDebugEnabled()) {
            log.debug("Request path={} durationMs={}", joinPoint.getSignature().toShortString(), elapsedMs);
        }
        return result;
    }
}
