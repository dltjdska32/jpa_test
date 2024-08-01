package jpashop.jpabook.repository;

import jakarta.persistence.EntityManager;
import jpashop.jpabook.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    @Transactional
    public void save(Item item) {
        //아이템이 없으면 저장
        if(item.getId() == null) {
            em.persist(item);
        }
        // 있으면 업데이트
        // 해당 else 문은 아이템의 아이디가 있을때 실행 -> 상품 수정(update)시 else문 실행
        // update의 경우 변경감지(derty check)나 병합(merge)를 사용한다.

        // 변경감지의 경우 데이터 베이스에 있는 값을 가져와(준영속) 영속상태로 만들고 트랜잭션 커밋 시점에 동작해서
        // 데이터베이스에 update 쿼리를 실행 -> 특정 필드값만 변경한다고 한다면 이전 데이터에서 변경하지 않은값은 그대로 저장되어있음
        // 특정 필드값을 변경해도 기존에 저장된 데이터에서 수정하지 않은 필드의 값은 변경되지 않음.

        // 병합(merge)의 경우 변경감지와 같이 데이터베이스에 있는 값을 가져와(준영속)  영속상태로 만듦
        // 하지만 merge의 경우 기존 데이터를 새로 입력받은 파라미터(객체)로 대체시켜버림.
        // 이경우 만약 새로입력받은 파라미터(객체)에 특정 필드값이 없다면, 해당 필드값을 null로 채워서 commit해버림.
        // 따라서 데이터베이스를 새로운 파라미터(객체)로 통째로 갈아버림

        // 그렇기 때문에 되도록이면 엔티티를 변경할때 merge를 쓰지 않고 변경감지를 활용해야한다.
        else {
            em.merge(item);
        }
    }


    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return  em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
