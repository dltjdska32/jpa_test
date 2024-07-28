package jpashop.jpabook.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    //이름을 필수로 받기위해 notEmpty 어노테이션 사용
    //값이 비어있으면 오류 발생
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
