package jpashop.jpabook.domain;

import jakarta.persistence.*;
import jpashop.jpabook.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; // 주문 수량

    //__ 생성 메서드__//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //아이템엔티티에 있는 전체 수량에서 주문 수량을뺌.
        item.removeStock(count);
        return orderItem;
    }


    //__ 비즈니스로직 __//
    //order에서 주문을 취소하면 재고수량을 원상복구
    public void cancel() {
        // 재고 수량을 원상 복구한다.
        getItem().addStock(count);
    }

    //__조회 로직__//

    /**
     * 주문 상품 전체 가격조회
     * */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
