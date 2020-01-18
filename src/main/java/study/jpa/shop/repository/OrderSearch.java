package study.jpa.shop.repository;

import lombok.Getter;
import lombok.Setter;
import study.jpa.shop.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

}
