package restaurant.petproject.service;

import org.springframework.stereotype.Service;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.Order;
import restaurant.petproject.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    List<Order> getOrderByStatus(String status);
    Order updateOrderStatus(Long id, String status);
    public Order createOrder(User user, Set<CartItem> items, Integer totalPrice);
}
