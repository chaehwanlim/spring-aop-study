package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

    @Before("hello.aop.order.aop.Pointcuts.serviceMethodsInOrderDomain()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[Before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.serviceMethodsInOrderDomain()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AfterReturning] {}, return = {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.serviceMethodsInOrderDomain()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[AfterThrowing] {}, message = {}", joinPoint.getSignature(), ex);
    }

    @After(value = "hello.aop.order.aop.Pointcuts.serviceMethodsInOrderDomain()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[After] {}", joinPoint.getSignature());
    }

}
