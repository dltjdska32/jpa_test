package jpashop.jpabook.domain.item;

import jakarta.persistence.*;
import jpashop.jpabook.domain.Category;
import jpashop.jpabook.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
// 상속된 객체 구분을 위함 어노테이션
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //엔티티  자체에서 해결할 수 있는 로직은 엔티티안에 넣어줌.
    //__ 비즈니스 로직__//

    /**
     *
     *  재고 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int quantity) {
       int restStock = this.stockQuantity - quantity;
       if(restStock < 0) {
           throw new NotEnoughStockException("need more stock");
       }
       this.stockQuantity = restStock;
    }

}
