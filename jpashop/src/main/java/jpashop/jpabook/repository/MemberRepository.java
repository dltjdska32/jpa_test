package jpashop.jpabook.repository;

import jakarta.persistence.EntityManager;
import jpashop.jpabook.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

//    public Long save(Member member) {
//        em.persist(member);
//        return member.getId();
//    }
//
//    public Member find(Long id) {
//        return em.find(Member.class, id);
//    }



    //.persist() 를 통해서 객체를 엔티티를 영속화
    //  그 후, commit시에 디비에 저장
    public void save(Member member) {
        em.persist(member);
    }

    // 멤버 아이디(pk)를 통해서 데이터를 찾아옴.
    public Member findOne(Long id ) {
        return em.find(Member.class, id);
    }

    //회원 목록을 뿌리기위한 함수 모든 회원의 정보를 가져옴.
    public List<Member> findAll(){
        //jpquery 함수의 첫번째 변수는 쿼리문 두번째 변수는 반환 타입
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //where name = :name  :name을 통해 입력받은 파라미터 적용
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
