package jpashop.jpabook.service;

import jpashop.jpabook.domain.Delivery;
import jpashop.jpabook.domain.Member;
import jpashop.jpabook.domain.Order;
import jpashop.jpabook.domain.OrderItem;
import jpashop.jpabook.domain.item.Item;
import jpashop.jpabook.repository.ItemRepository;
import jpashop.jpabook.repository.MemberRepository;
import jpashop.jpabook.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;
    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = null;
        try {
            item = itemRepository.findOne(itemId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //배송정보 생성
        Delivery delivery = new Delivery();
        //멤버 객체에있는 주소를 사용
        delivery.setAddress(member.getAddres());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    // 주문취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문엔터티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    //주문 검색
//    public List<Order> findOrders(OrderSearch oderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}

