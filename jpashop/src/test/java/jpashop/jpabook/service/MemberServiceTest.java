package jpashop.jpabook.service;

import jakarta.persistence.EntityManager;
import jpashop.jpabook.domain.Member;
import jpashop.jpabook.repository.MemberRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private EntityManager em;
//    @Autowired
//    MemberService memberService;
//    @Test
//    @Transactional
//    void 회원_조회() {
//        Member member = new Member();
//        member.setUserName("member 1");
//        Long savedId = memberService.save(member);
//        Member mem = memberService.findMem(savedId);
//        Assertions.assertThat(mem.getUserName()).isEqualTo(member.getUserName());
//    }


    @Test
    public void 회원가입() throws Exception {

        Member member = new Member();
        member.setName("Kim");

        Long savedId = memberService.join(member);


        Assertions.assertThat(savedId).isEqualTo(memberRepository.findOne(savedId).getId());
    }



    @Test
    public void 중복회원예외() throws Exception {
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName("Kim");
        member2.setName("Kim");

        memberService.join(member1);
//
//        try{
//            memberService.join(member2);
//        } catch (IllegalStateException e){
//            System.out.println("오류발생");
//        }


        //junit 5 예외처리
        Assertions.assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }


}
