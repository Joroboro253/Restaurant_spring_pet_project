package restaurant.petproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.petproject.entity.*;
import restaurant.petproject.repository.OrderRepository;
import restaurant.petproject.repository.ShoppingCartRepository;
import restaurant.petproject.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public Order createOrderFromCart(ShoppingCart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(new Date());
        order.setStatus("Pending");

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setDish(cartItem.getDish());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        shoppingCartRepository.delete(cart);
        return savedOrder;
    }

    @Override
    public Order createOrder(User user, Set<CartItem> items, Integer totalPrice) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : items) {
            OrderItem orderItem = new OrderItem(null, cartItem.getDish(), cartItem.getQuantity(), cartItem.getDish().getPrice());
            orderItems.add(orderItem);
        }
        Order order = new Order(user, orderItems, totalPrice, "PENDING");
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order); // Установить ссылку на заказ для каждого элемента заказа
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrderByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }
}