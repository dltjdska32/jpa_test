//package jpashop.jpabook.controller;
//
//import jpashop.jpabook.entity.Member;
//import jpashop.jpabook.repository.MemberRepository;
//import jpashop.jpabook.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequiredArgsConstructor
//public class MemberController {
//    private final MemberService memberService;
//
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
//
//
//}
