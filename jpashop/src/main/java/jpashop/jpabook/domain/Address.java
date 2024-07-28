package jpashop.jpabook.domain;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


// 값 타입
// 값 타입은 변경 불가능 하게 생성해야한다.
@Getter
// 값타입을 정의하는 곳
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
