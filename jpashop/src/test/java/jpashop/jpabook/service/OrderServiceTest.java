package jpashop.jpabook.service;

import jakarta.persistence.EntityManager;
import jpashop.jpabook.domain.Address;
import jpashop.jpabook.domain.Member;
import jpashop.jpabook.domain.Order;
import jpashop.jpabook.domain.OrderStatus;
import jpashop.jpabook.domain.exception.NotEnoughStockException;
import jpashop.jpabook.domain.item.Album;
import jpashop.jpabook.domain.item.Book;
import jpashop.jpabook.domain.item.Item;
import jpashop.jpabook.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    EntityManager em;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //멤버, 아이템중 책 설정.
        Member member = createMember();


        Book book = createBook("시골 JPA", 10000, 10);
        Album album = createAlbum("시골 JPA", 10000, 10);

        // 주문생성
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //order클래스에 oderCreate에서 오더아이템을 여러개 받을수 있음  order service 에서는 itemid 하나받음 어떻게 item여러개 받음?
        // 한명이 책클래스 여러개 주문한지 어떻게앎?

        // 주문 확인 (데이터 베이스 확인)
        Order getOrder = orderRepository.findOne(orderId);


        //첫번째인자 기대값, 두 번째인자 실제값, 세번째 인자 메세지
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다.");

    }




    @Test
    public void 주문취소() throws Exception {
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 CANCEL 상태여야함");
        assertEquals(10, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야함.");

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

    //  .isInstanceOf(NotEnoughStockException.class) 예상되는 예외 클래스
        // 책의 재고는 10개 주문은 11개를 시켰으므로 재고 수량 부족 예외 발생 따라서 테스트 통과
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), 11))
                .isInstanceOf(NotEnoughStockException.class)
        ;
      //  fail("재고 수량 부족 예외가 발생해야함. 테스트 실패"); 여기선 필요없음.
    }

    private Book createBook(String name, int price, int stockQauntity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQauntity);
        em.persist(book);
        return book;
    }

    private Album createAlbum(String name, int price, int stockQauntity) {
        Album book = new Album();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQauntity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}