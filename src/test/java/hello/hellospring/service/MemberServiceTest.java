package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    // clear()를 해주고 싶은데 MemberService()밖에 없어서 clear()가 안된다.
    // -> MemoryMemberRepository를 가져와야 한다.
    // MemberService memberService = new MemberService();
    // MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    // 레포지토리 객체를 하나만 만들어서 쓰기
    MemberService memberService;
    MemoryMemberRepository memberRepository;
    // 테스트 메서드 한개 실행 전마다 객체를 각각 생성해준다. -> 테스트는 독립적으로 실행이 되어야 하기 때문에
    // (그전엔 왜 안그럼?ㅠㅠ) 그냥 이번에 할때 기왕이면 다홍치마라고 넣어준건가..
    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach              // 메서드가 실행이 끝날 때마다 어떤 동작을 하게 해주는 어노테이션
    public void afterEach(){    // 이녀석은 이제 콜백 메서드의 기능을 하게 된다.
        memberRepository.cleasrStore(); // store라는 Map<Long, Member>을 전부 비워준다.
        // store : repository의 모든 메서드에서 Member 객체 관련 값을 반환해주기 위해 사용하는 (아이디, 객체) 형태의 저장소
    }

    // 또하나의 문제점 제기
    // new MemberService(); 할 때
    // private final MemberRepository memberRepository = new MemoryMemberRepository(); 가 생긴다.
    // 그런데 위에서 clearStore() 쓰려고 만든 녀석도 있는데, 둘은 다른 객체
    // 두 개를 만들어서 쓸 이유는 없다. - 다른 인스턴스이기 때문에 내용물이 달라지거나 할 수 있음



    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("hello");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }
    // 테스트는 정상 플로우도 중요한데 예외 플로우도 훨씬 더 중요하다.
    // 회원가입 테스트는 중복회원으로 예외를 터트려 보는것도 중요하다.
    @Test
    public void 중복_회원_예외(){
        // given
        Member member1 = new Member();
        member1.setName("spring");
        // 같은 이름으로 2번 Member만드는 것까지는 사실 가능
        Member member2 = new Member();
        member2.setName("spring");
        // when
        // join을 두번 해보겠다.
        // 첫번재 회원가입
        memberService.join(member1);
        // 방법1. 에러가 나는 것을 그냥 눈으로 확인
        // memberService.join(member2);  -> 이거 하는 순간 에러가 나타남
        /* 이렇게 하기로 했었기 때문에
         memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
        */

        // 방법2.에러가 나는 것을 눈으로 확인하지 않고 try catch로 잡아준다.
 /*       try{
            memberService.join(member2);
            // 안에 메시지로 "예외가 발생해야 합니다." 이렇게 넣어줄 수도 있다.
            fail();
        }catch (IllegalStateException e){
            // 예외가 정상적으로 터져서 성공
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
  */
        // 방법3. 예외를 던지는 것을 확인한다.
        // try catch 넣는것이 다소 애매. => assertThrows()라는 것을 사용
                     // 이 에러가 터지는지 확인          // 이 메소드를 실행할 때
        // assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        // [RuntimeException] IllegalStateException 메소드가 요구된 처리를 하기에 적합한 상태에 있지 않을 때 나타나는 에러

        // 방법4.
        // 메시지를 어떻게 알아? 반환해서 Ctrl Alt V assertThat.isEqualTo로 확인한다.
       IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
       assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");



        // then

    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}