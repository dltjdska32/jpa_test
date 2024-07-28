package jpashop.jpabook.controller;

;
import jakarta.validation.Valid;
import jpashop.jpabook.domain.Address;
import jpashop.jpabook.domain.Member;
import jpashop.jpabook.repository.MemberRepository;
import jpashop.jpabook.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

//    @GetMapping("/memLogin")
//    public String login() {
//         return "login";
//    }
//
//    @PostMapping("/memLogin")
//    public String login(@RequestParam(name = "id") String username) {
//        Member member = new Member();
//        member.setUserName(username);
//        memberService.save(member);
//        return "redirect:/memLogin";
//    }
//

    //모델은 데이터를 담고 컨트롤러를 통해 뷰로 옮겨준다.
    @GetMapping("/members/new")
    public String createForm(Model model) {
        log.info("Create form");
        //memberForm이라는 이름으로 MemberForm()객체를 뷰에 전달 화면에서 객체에 접근이 가능.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }


    @PostMapping("/members/new")
    // @valid를 통해서 form 을 검증한다 form에는 이름이 필수로 입력 되어야 한다.
    // bindingresult는 스프링이 제공하는 기능
    // form에 오류가 있으면 원래는 팅겨버리는데 validate한것 뒤에 bindingresult가있으면 에러가 담겨서 해당 함수가 실행된다.
    public String create(@Valid MemberForm form, BindingResult result) {


        // 만약 result에 에러가 담겨서 온다면 멤버폼을 다시반환
        // 멤버폼을 다시반환할때 기존에 입력받은 데이터와 result도 같이 가져간다.

        //   <label th:for="name">이름</label>
        //            <input type="text" th:field="*{name}" class="form-control" placeholder="이름을 입력하세요"
        //                   th:class="${#fields.hasErrors('name')}? 'form-control fieldError' : 'form-control'">
        //            <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
        // 멤버 폼html에서  이름에 오류가 발생하면 input의 테두리를 빨간색으로 바꾸고 , p태그를 띄운다. ->  th:errors="*{name}"이름과 관련된 에러를 출력해줌.
        //    @NotEmpty(message = "회원 이름은 필수 입니다.") message출력 + input 태그에 입력된 기존 데이터 출력.

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        //폼을 통해 입력받은 값들을 통해 객체를 만든다.
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        //만든객체를 통해 영속성 컨텍스트에 persist한다.
        memberService.join(member);

        //회원가입을 완료하면 초기화면으로 보내준다.
        return "redirect:/";
    }

    //추가
    @GetMapping(value = "/members")
    public String list(Model model) {
        //강의에서는 엔터티를 직접 넘기지만. 실무에서는 직접 넘기는걸 권장하지 않음.
        //실무나 api등 을 만들때는 dto 객체나 form객체로 변환하여 넘겨야한다.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }


}
