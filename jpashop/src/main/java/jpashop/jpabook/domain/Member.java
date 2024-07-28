package jpashop.jpabook.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //오더 테이블과 연관관계설정
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
