package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

// 가져다 쓸게 아니기 때문에 굳이 public으로 안해도 된다
class MemoryMemberRepositoryTest {
    // MemberRepository repository = new MemoryMemberRepository();

    // MemoryMemberRepository()만 테스트 하는 상황이기 때문에 인터페이스를 클래스로 변경
    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트 끝날 때마다 레퍼지토리를 깔끔하게 지워주는 메서드를 만든다.
    @AfterEach              // 메서드가 실행이 끝날 때마다 어떤 동작을 하게 해주는 어노테이션
    public void afterEach(){    // 이녀석은 이제 콜백 메서드의 기능을 하게 된다.
        repository.cleasrStore(); // store라는 Map<Long, Member>을 전부 비워준다.
        // store : repository의 모든 메서드에서 Member 객체 관련 값을 반환해주기 위해 사용하는 (아이디, 객체) 형태의 저장소
    }

    // 이렇게 두고 돌리면 그냥 이 메서드가 실행된다.(마치 메인 메소드 쓰는 것과 비슷)
    @Test       // 메모리멤버레포지토리 클래스의 메소드와 별개의 메소드(해당 클래스의 save를 테스트 한다는 뜻)
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        // Optional<Member> byId = repository.findById(member.getId());
        // Optional에서 값을 꺼내올 때에는 get()으로 값을 꺼낼 수 있다 - 이렇게 get으로 바로 꺼내는게 좋은것은 아니지만 testcode니까..
        Member result = repository.findById(member.getId()).get();

        // 검증하기 : new로 만든 member와 DB에서 꺼낸 member가 같으면 참, 다르면 거짓
        // 단순하게 이렇게 비교해도 된다(객체와 객체인데 가능한건진 모르겠음 ㅠㅠ)
        // 1. System.out.println("result = " + (result == member));

        // 계속 이렇게 글자로 볼 수는 없다, 어썰션스라는 기능을 사용하자 : 둘이 같은지 확인해 볼 수 있음
        // expected, actual (ctrl P) - 기대하는 값, 현재 여기의 값
        // 테스트케이스에서 만든 데이터 : member, DB에서 꺼낸 데이터 : result
        // 2. org.junit.jupiter.api; 사용
        // Assertions.assertEquals(result, member);
        // 오류가 없다면, 콘솔에 뜨는 내용은 없지만 녹색 체크
        // 오류가 있다면, 콘솔에 붉은색으로 에러 메시지가 뜨고 x아이콘

        // 3. 요즘에는 org.assertj.core.api.Assertions; 를 써서 좀더 편하게
        // actual, expected (여기 값, ~이었으면 하는 값)
        // 여기 클래스의 값이 DB에서 뽑아온 값과 같았으면
        // Assertions.assertThat(member).isEqualTo(result);
        // alt + Enter누르고 import하면 static이기 때문에 그냥 쓸 수 있음
        assertThat(member).isEqualTo(result);

        // 실무에서는 이것을 빌드툴이랑 엮어서 빌드툴에서 빌드할때
        // 테스트케이스를 통과하지 않고 오류가 나오면 다음 단계로 못넘어가게 막아버린다.
    }
    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        // 좀 더 정교한 테스트를 위해서 멤버2를 만들어본다.
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1);
    }
    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member1.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        // actual=expecting, expected (여기 값, ~이었으면 하는 값)
        // 여기 클래스의 값이 DB에서 뽑아온 값과 같았으면 인거 같은데...사람마다 스타일인듯
        // DB에서 뽑은 것이, 여기 클래스의 값2와 같아야지..!하고 생각할수도 있으니
        assertThat(result.size()).isEqualTo(2);
    }
    // 전체 테스트를 실행하면, findAll()이 먼저 테스트 진행될 경우
    // "spring1"이라는 이름을 가진 객체와 "spring2"라는 이름을 가진 객체 두 개가 만들어진다.
    // 그 다음 findByName()이 실행될 경우 "spring1"이라는 이름을 가진 객체를 찾아
    // findByName()에서 만들어진 "spring1" 이름의 객체와 비교하는 것인데
    // 이 때 findAll()에서 만들어진 "spring1"객체가 튀어나와서 비교를 하게되면 같지 않아서 Text failed를 돌려준다.
    // -> 해결 방법 : 테스트가 하나 끝나고 나면 데이터를 클리어 해줘야 한다.
    // ※ 테스트는 서로 순서와 관계없이, 서로 의존관계 없이 설계가 되어야 한다.
    // => 하나의 테스트가 끝날 때마다 저장소나 공용 데이터들을 깔끔하게 지워줘야 한다.
}
