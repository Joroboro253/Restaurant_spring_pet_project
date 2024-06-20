package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;
import restaurant.petproject.payment.LiqPayPayment;
import restaurant.petproject.service.ShopService;

@Controller
public class PaymentController {

    @Autowired
    private LiqPayPayment liqPayPayment;

    @Autowired
    private ShopService shopService;

    @GetMapping("/payment")
    public String paymentForm(HttpSession session, Authentication auth, Model model) {
        ShoppingCart shop = shopService.getShoppingCartByUser((User) auth.getPrincipal());
        if (shop == null || shop.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        String amount = String.valueOf(shop.getTotalPrice());
        String currency = "UAH";
        String description = "Payment for order " + shop.getId();
        String orderId = "order_id_" + System.currentTimeMillis();

        String serverUrl = "https://8b15-159-224-20-62.ngrok-free.app/liqpay-callback";
        String returnUrl = "http://localhost:8082/";

        String paymentFormHtml = liqPayPayment.createPayment(amount, currency, description, orderId, serverUrl, returnUrl);

        model.addAttribute("paymentFormHtml", paymentFormHtml);
        return "auto-submit-payment-form";
    }
}

