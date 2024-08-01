package jpashop.jpabook.repository;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpashop.jpabook.domain.Order;
import jpashop.jpabook.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //동적쿼리 1. 파라미터를 문자열에 각각 추가
    public List<Order> findAllByString(OrderSearch orderSearch) {


        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            //처음추가할때 where을 넣어줌
            if(isFirstCondition) {
                jpql += " where ";
                isFirstCondition = false;
            }
            // 두번째 추가시 and를 넣어줌
            else {
                jpql += " and ";
            }
            jpql += "o.status = :status";
        }

        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if(isFirstCondition) {
                jpql += " where ";
                isFirstCondition = false;
            } else {
                jpql += " and ";
            }

            jpql += "m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();


    }


    //동적 쿼리 2. jpa criteria 사용 -> 실무에서 사용하기 어려움
    public List<Order> findAllByCriteria (OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원이름검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

        //querydsl 사용

}
