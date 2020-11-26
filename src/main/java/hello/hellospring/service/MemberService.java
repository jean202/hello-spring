package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Service
public class MemberService {
    // 테스트 시에도 new 를 해주기 때문에 같은 객체를 사용할 수 있도록 바꾼다.
    // private final MemberRepository memberRepository = new MemoryMemberRepository();

    private final MemberRepository memberRepository;
    // 생성자를 만들어서 외부에서 객체를 만들어서 넣어 주는 것으로 변형
    // 멤버서비스 입장에서 멤버리퍼지토리 객체를 외부에서 넣어줘서, 해당 외부에 의존된다.
    // Dependency Injection, 의존주입
    @Autowired // 멤버서비스는 멤버리포지토리가 필요하다
    public MemberService(MemberRepository memberRepository) {
        // 아 너는 멤버리포지토리가 필요하구나 하고 스프링 컨테이너에 있는 구현체 메모리멤버리포지토리를 딱 넣어준다.
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입 : rule - 같은 이름이 있는 회원이 있으면 가입이 안된다.
     */
    public Long join(Member member){
        // 같은 이름의 중복 회원X

        // 기가막히는 단축키 : ctrl alt v - 참조변수를 만들어 준다.
        /* 옵셔널이 보이면 안이쁘다
        Optional<Member> result = memberRepository.findByName(member.getName());
        // Optional<Member>가 null이 아니고 값이 있으면 - 멤버에 값이 있으면
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        */
        // Ctrl Alt Shift T : Refactoring과 관련된 단축키가 나온다.
        // extract method : 연결 연결된 표현을 메소드로 만들어 준다.
        validateDuplicateMember(member); // 중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }
    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 하나의 회원 조회 - memberId를 넘겨서 Member객체 찾아서 꺼내기
     */
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

}
