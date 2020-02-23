package study.jpa.shop.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.jpa.shop.api.query.OrderQueryDto;
import study.jpa.shop.api.query.OrderQueryRepository;
import study.jpa.shop.domain.Address;
import study.jpa.shop.domain.Order;
import study.jpa.shop.domain.OrderItem;
import study.jpa.shop.domain.OrderStatus;
import study.jpa.shop.repository.OrderRepository;
import study.jpa.shop.repository.OrderSearch;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderRepository.findAll(new OrderSearch())
            .stream()
            .map(OrderDto::new)
            .collect(toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        // collect 을 fetch 조인하면 paging 을 할 수 없다. -> 모든 list 를 호출한 후에 memory 에서 잘라내기 때문
        return orderRepository.findAllWithItem()
            .stream()
            .map(OrderDto::new)
            .collect(toList());
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {

        return orderRepository.findAllWithMemberDelivery(offset, limit)
            .stream()
            .map(OrderDto::new)
            .collect(toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findOrderQueryDtos_optimizer();
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                            .map(OrderItemDto::new)
                            .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
