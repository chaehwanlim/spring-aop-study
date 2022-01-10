package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV5Order {

    @Aspect
    @Order(2)
    public static class LogAspect {

        @Around("hello.aop.order.aop.Pointcuts.orderDomain()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

    }

    @Aspect
    @Order(1)
    public static class TransactionAspect {

        @Around("hello.aop.order.aop.Pointcuts.serviceMethodsInOrderDomain()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[Transaction started] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[Transaction committed] {}", joinPoint.getSignature());

                return result;
            } catch (Exception e) {
                log.info("[Transaction rollback] {}", joinPoint.getSignature());

                throw e;
            } finally {
                log.info("[Resource released] {}", joinPoint.getSignature());
            }
        }

    }

}