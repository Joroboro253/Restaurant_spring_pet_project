package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import restaurant.petproject.entity.Order;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.User;
import restaurant.petproject.service.impl.OrderServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final ShopServiceImpl shopServiceImpl;

    @Autowired
    public OrderController(OrderServiceImpl orderService, UserServiceImpl userService, ShopServiceImpl shopServiceImpl) {
        this.orderService = orderService;
        this.userService = userService;
        this.shopServiceImpl = shopServiceImpl;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id).orElse(null);
        model.addAttribute("order", order);
        return "order-detail";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam Long userId, @RequestParam Set<CartItem> items, Model model) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (!userOptional.isPresent()) {
            model.addAttribute("error", "User not found");
            return "error";
        }
        User user = userOptional.get();
        Integer totalPrice = items.stream().mapToInt(CartItem::getSubtotal).sum();
        Order order = orderService.createOrder(user, items, totalPrice);
        model.addAttribute("order", order);
        return "redirect:/orders/payment/" + order.getId();
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status, Model model) {
        Order order = orderService.updateOrderStatus(orderId, status);
        model.addAttribute("order", order);
        return "order-detail";
    }

//    @GetMapping("/payment/{orderId}")
//    public String processPayment(@PathVariable Long orderId, Model model) {
//        Order order = orderService.getOrderById(orderId).orElse(null);
//        if (order == null) {
//            model.addAttribute("error", "Order not found");
//            return "error";
//        }
//        // Здесь должно быть перенаправление на страницу оплаты LiqPay
//        // Например:
//        // return "redirect:/liqpay/payment/" + orderId;
//        model.addAttribute("order", order);
//        return "payment";
//    }

    @GetMapping("/payment-success/{orderId}")
    public String paymentSuccess(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId).orElse(null);
        if (order == null) {
            model.addAttribute("error", "Order not found");
            return "error";
        }
        orderService.updateOrderStatus(orderId, "PAID");
        shopServiceImpl.clearCart(order.getUser());
        model.addAttribute("order", order);
        return "redirect:/orders/" + orderId;
    }
}


