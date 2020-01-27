package study.jpa.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jpa.shop.domain.Order;
import study.jpa.shop.repository.OrderRepository;
import study.jpa.shop.repository.OrderSearch;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    
    private final OrderRepository orderRepository;
    
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        //무한 루프에 빠진다.(json 변환 과정에서 서로 연관관계에 있는 객체를 호출)
        //엔티티를 직접 노출하면 많은 문제가 생긴다.
        List<Order> all = orderRepository.findAll(new OrderSearch());
        return all;
    }
}
