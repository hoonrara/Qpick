package com.quiz.quiz_app.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    // ✅ com.quiz.quiz_app.controller 하위 모든 메서드에 적용
    @Around("execution(* com.quiz.quiz_app.controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("▶️ 요청: {}", methodName);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("✅ 완료: {} ({}ms)", methodName, endTime - startTime);
            return result;
        } catch (Exception e) {
            log.error("❌ 예외 발생: {} - {}", methodName, e.getMessage());
            throw e;
        }
    }
}
