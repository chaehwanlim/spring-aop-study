package hello.aop.proxycompetition;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);    // JDK 동적 프록시로 설정

        // 프록시를 인터페이스로 캐스팅
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // 프록시를 구현체로 캐스팅 시도 -> 에러가 난다. 인터페이스 구현으로 프록시가 생성되었기 때문이다.
        assertThatThrownBy(
                () -> {
                    MemberServiceImpl memberServiceImplProxy = (MemberServiceImpl) memberServiceProxy;
                }
        ).isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);    // CGLIB 프록시로 설정

        // 프록시를 인터페이스로 캐스팅 시도 -> 부모 타입이기 때문에 가능하다.
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // 프록시를 구현체로 캐스팅 시도 -> 애초에 구현체를 상속받아 프록시를 만들었기 때문에 당연히 가능하다.
        MemberServiceImpl memberServiceImplProxy = (MemberServiceImpl) memberServiceProxy;
    }

}
