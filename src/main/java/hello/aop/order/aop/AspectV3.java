package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    // hello.aop.order 패키지 포함 및 내부 모두 포함
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {}

    // 클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    private void allServiceMethods() {}

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    // hello.aop.order 패키지와 하위 패키지에 속해 있으면서 클래스 이름 패턴이 Service
    @Around("allOrder() && allServiceMethods()")
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
