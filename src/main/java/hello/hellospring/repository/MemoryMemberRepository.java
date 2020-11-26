package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class MemoryMemberRepository implements MemberRepository{

    // 실무에서는 동시성 문제가 있을 수 있어서 동류되는 변수일 때에는 컨쿼런트해쉬맵을 써야한다.
    private static Map<Long, Member> store = new HashMap<>();
    // 실무에서는 동시성 문제를 고려해서 어텀 롱 등을 써야 한다.
    private static long sequence = 0L;

    // name은 parameter로 넘어온 상태(고객이 회원가입 할 때 입력)
    @Override
    public Member save(Member member) {
        member.setId(++sequence); // store에 넣기 전에 member의 id값을 세팅해 주고
        // [방금 1 올린 아이디 값 가져오고(ex: 3) , 그 id에 해당하는 현 member]를 store라는 Map에 저장
        store.put(member.getId(), member);
        return member; // 처리를 끝낸 member(저장된 결과)를 반환
    }

    @Override               // store에서 꺼내기
    public Optional<Member> findById(Long id) { // id 값으로 member 찾기
        // store.get(id)의 결과가 null일 수도 있으면 Optional로 감싼다 - 너러블이 감싸준다 : 감싸서 반환해주면 클라이언트에서 뭘 할수있다
        return Optional.ofNullable(store.get(id)); // Map에 (id, member)로 저장되어 있는 member를 키값(id)로 찾아온다.
    }

    @Override                       // 파라미터로 넘어온 name
    public Optional<Member> findByName(String name) {
        return store.values().stream() // store의 값들-member들-에서 공백을 제거
                //member에서 name을 가져온 것이 파라미터로 넘어온 name이랑 같은지 확인(filter가 루프로 돌려준다)
                .filter(member -> member.getName().equals(name)) // ramda  : 같은 경우에만 필터링이 된다.
                .findAny(); // 루프를 돌리다가 찾으면 반환 findAny : 하나라도 찾는 것.
                // 끝까지 돌려도 없으면 메소드의 결과가 Optional에 null이 포함되어 반환됨
    }

    // store는 Map인데 List로 반환하도록 되어 있는 이유 : 자바에서 실무할때 List를 많이 쓴다. 루프 돌리기 편하다.
    @Override
    public List<Member> findAll() {
                            // member들을 ArrayList로 반환
        return new ArrayList<>(store.values());
    }

    // 레퍼지토리가 제대로 동작하는지 검증하는 방법 : 확인하는 기가막히는 방법이 바로 테스트케이스를 작성하는것

    // 테스트 끝날 때마다 레퍼지토리를 깔끔하게 지워주는 메서드를 만든다.
    public void cleasrStore(){
        store.clear(); // store라는 Map<Long, Member>을 전부 비워준다.
    }

}
