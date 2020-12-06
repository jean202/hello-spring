package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller  // MemberController를 스프링 컨테이너가 뜰 때 생성을 해준다.
public class MemberController {
   // 스프링 컨테이너에 등록을 하고 사용하겠다.
    private final MemberService memberService;

    @Autowired  // MemberController를 생성할 때 생성자가 호출되는데,생성자에 Autowired가 있으면
    // 스프링이 스프링 컨테이너에 있는 memberService를 가져다가 연결을 시켜준다.
    public MemberController(MemberService memberService) { // 빨간불이 뜬 이유 : MemberService could not be found
                                        // memberService가 스프링 빈으로 등록되어 있지 않기 때문에
                                        // 의존관계를 주입해 준다 : Dependency Injection
        this.memberService = memberService;
    }
    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm"; // 아무것도 안하고 createMemberForm이라는 주소로 이동만 한다.
        // GetMapping에서 리턴하면 템플릿에서 찾는다. createMemberForm.html을 띄워 줌
    }
    @PostMapping("/members/new")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);
        return "redirect:/"; // 회원가입이 끝나면 홈 화면으로 보낸다.
    }
    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
