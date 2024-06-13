package restaurant.petproject.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import restaurant.petproject.entity.Order;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;
import restaurant.petproject.payment.LiqPayPayment;
import restaurant.petproject.service.impl.OrderServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final ShopServiceImpl shopServiceImpl;
    private final LiqPayPayment liqPayPayment;

    @Autowired
    public OrderController(OrderServiceImpl orderService, UserServiceImpl userService, ShopServiceImpl shopServiceImpl, LiqPayPayment liqPayPayment) {
        this.orderService = orderService;
        this.userService = userService;
        this.shopServiceImpl = shopServiceImpl;
        this.liqPayPayment = liqPayPayment;
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
    public String createOrder(Authentication auth, Model model) {
        User user = (User) auth.getPrincipal();
        ShoppingCart cart = shopServiceImpl.getShoppingCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("error", "Your cart is empty");
            return "error";
        }

        Integer totalPrice = cart.getItems().stream().mapToInt(CartItem::getSubtotal).sum();
        Order order = orderService.createOrder(user, new HashSet<>(cart.getItems()), totalPrice);

        // Redirect to payment processing
        return "redirect:/orders/payment/" + order.getId();
    }

    @PostMapping("/initiate-payment")
    public String initiatePayment(Authentication auth, Model model) {
        User user = (User) auth.getPrincipal();
        ShoppingCart cart = shopServiceImpl.getShoppingCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("error", "Your cart is empty");
            return "error";
        }

        Integer totalPrice = cart.getItems().stream().mapToInt(CartItem::getSubtotal).sum();
        Order order = orderService.createOrder(user, new HashSet<>(cart.getItems()), totalPrice);

        // LiqPay Data
        String amount = String.valueOf(order.getTotalPrice());
        String currency = "UAH";
        String description = "Payment for order " + order.getId();
        String orderId = "order_id_" + System.currentTimeMillis();

        String serverUrl = "https://yourserver.com/liqpay-callback"; // URL для серверного уведомления
        String returnUrl = "http://localhost:8082/orders/payment-success/" + order.getId(); // URL перенаправления после оплаты

        String paymentFormHtml = liqPayPayment.createPayment(amount, currency, description, orderId, serverUrl, returnUrl);

        // Передаем форму LiqPay на клиентскую сторону для автоматической отправки
        model.addAttribute("paymentFormHtml", paymentFormHtml);
        return "auto-submit-payment-form";
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status, Model model) {
        Order order = orderService.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderService.save(order);
        }
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
//
//        String amount = String.valueOf(order.getTotalPrice());
//        String currency = "UAH";
//        String description = "Payment for order " + order.getId();
//        String orderIdStr = "order_id_" + System.currentTimeMillis();
//
//        String serverUrl = "https://yourserver.com/liqpay-callback";
//        String returnUrl = "https://yourwebsite.com/orders/payment-success/" + order.getId();
//
//        String paymentFormHtml = liqPayPayment.createPayment(amount, currency, description, orderIdStr, serverUrl, returnUrl);
//
//        model.addAttribute("paymentFormHtml", paymentFormHtml);
//        return "auto-submit-payment-form";
//    }

    @Transactional
    @GetMapping("/payment-success/{orderId}")
    public String paymentSuccess(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId).orElse(null);
        if (order == null) {
            model.addAttribute("error", "Order not found");
            return "error";
        }
        // Updating status transaction
        orderService.updateOrderStatus(orderId, "PAID");

        // Cleaning user cart
        ShoppingCart cart = shopServiceImpl.getShoppingCartByUser(order.getUser());
        if (cart != null) {
            cart.getItems().clear();
            shopServiceImpl.save(cart);
        }

        model.addAttribute("order", order);
        return "redirect:/orders/" + orderId;
    }
}



