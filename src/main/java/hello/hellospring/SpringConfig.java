package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    // 스프링이 뜰 때, 이 클래스 덕분에 memberService와 memberRepository를 둘 다 스프링 빈에 등록하고,
    // 스프링빈에 등록되어있는 멤버리포지토리를 멤버서비스에 넣어준다.
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
