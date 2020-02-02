package study.jpa.shop.api.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();
        result.forEach(o -> {
            o.setOrderItems(findOrderItems(o.getOrderId()));
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
            "select new study.jpa.shop.api.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
            "select new study.jpa.shop.api.query.OrderQueryDto(o.id, o.member.name, o.orderDate, o.status, o.delivery.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class)
            .getResultList();
    }

    public List<OrderQueryDto> findOrderQueryDtos_optimizer() {
        List<OrderQueryDto> result = findOrders();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
            "select new study.jpa.shop.api.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

        return orderItems.stream().collect(groupingBy(OrderItemQueryDto::getOrderId));
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(toList());
    }
}
