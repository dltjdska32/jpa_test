package jpashop.jpabook.service;

import jpashop.jpabook.domain.Member;
import jpashop.jpabook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

//    @Transactional
//    public Long save(Member member) {
//        Long savedID = memberRepository.save(member);
//        return savedID;
//    }
//
//    public Member findMem(Long id) {
//        Member savedMem = memberRepository.find(id);
//
//        return savedMem;
//    }




    //회원 가입
    //쓰기시 readonly = false
    @Transactional
    //실무에서는 같은 이름을 가진 회원이 동시에 회원가입시에 같은이름이 디비에 저장될수있다
    // 따라서 db에 해당 열을 유니크 제약조건을 걸어둠.
    public Long join(Member member) {
        //같은 이름의 회원은 안된다 (중복회원검증)
        validateDuplicateMember(member);

        memberRepository.save(member);

        //아이디 값을 가져와 저장되어있는지 확인
        return member.getId();
    }

    //중복회원일 경우 예외발생
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        //이름을 통해 가져온값이 비어있지 않으면 (값이 있으면)
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    //회원 전체 조회

    //읽기전용
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long MemberId) {
        return memberRepository.findOne(MemberId);
    }
}
