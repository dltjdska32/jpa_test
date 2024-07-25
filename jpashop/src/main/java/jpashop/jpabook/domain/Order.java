package jpashop.jpabook.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    //member table과 관계설정
    @ManyToOne
    //member_id 는 외래키
    @JoinColumn(name = "member_id")
    private  Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 order cancel


    //__ 연관관계 메서드__//

    // 양방향 연관관계 설정.
    // 주문생성시 해당 회원의 주문목록에도 이주문이 추가
    public void setMember(Member member) {
        this.member = member;
        // member의 oders 리스트에 주문을 추가
        member.getOrders().add(this);
    }

    // 양방향 연관관계 설정
    // 주문생성시 orderItem에 order(주문)객체 추가
    public void addOrderItem(OrderItem orderItem) {
        //orderItems 리스트에 주문아이템 추가
        orderItems.add(orderItem);
        //orderItem 클래스에 현재 order(주문) 추가
        orderItem.setOrder(this);
    }

    //양방향 연관관계 설정
    // 주문생성시 딜리버리 엔티티에도 이 주문목록이 추가
    public void setDelivery(Delivery delivery) {
        //delivery 설정
        this.delivery = delivery;
        //딜리버리 객체에 이 주문 설정
        delivery.setOrder(this);
    }

    //__생성메서드__//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }

        // order의 처음 상태를 order로 강제
        order.setStatus(OrderStatus.ORDER);
        //현재시간으로 시간을 잡음
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 엔티티에 비즈니스 로직을 추가한 패턴을 도메인 모델 패턴이라함.
    // 엔티티에는 비즈니스로직이 거의 없고 서비스계층에서 대부분 비즈니스 로직을 처리하면
    //트랜잭션 스크립트 패턴
    //__비즈니스 로직__//
    /**
     * 주문 취소
     */
    public void cancel() {
        // deliverystatus가 comp 면 취소불가
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은" +
                    " 취소가 불가능합니다.");
        }

        // orderstatus를 cancel로 설정
        this.setStatus(OrderStatus.CANCEL);

        // 오더 아이템에 cancel함 (수량 원상복구)
        // 오더에는 오더아이템이 여러개 있을수 있다. 오더아이템을 순회하면서 취소해준다. + item 수량원복
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //__조회 로직__//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }
}
