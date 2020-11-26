package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); // 회원 저장
    Optional<Member> findById(Long id); //Optional : null 값을 그대로 반환하지 않고 Optional이라는 것으로 감싸서 반환한다.
    Optional<Member> findByName(String name);
    List<Member> findAll(); // 지금까지 저장된 모든 회원 리스트 반환하기
}
