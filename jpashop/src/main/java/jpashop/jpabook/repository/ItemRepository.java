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
