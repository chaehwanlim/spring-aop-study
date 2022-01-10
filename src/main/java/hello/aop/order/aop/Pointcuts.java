package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class Pointcuts {

    // hello.aop.order 패키지 포함 및 내부 모두 포함
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void orderDomain() {}

    // 클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void serviceMethods() {}

    // allOrder & allService
    @Pointcut("orderDomain() && serviceMethods()")
    public void serviceMethodsInOrderDomain() {}

}
