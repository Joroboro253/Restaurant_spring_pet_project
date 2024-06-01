package restaurant.petproject.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;
import restaurant.petproject.payment.LiqPayCallbackHandler;
import restaurant.petproject.payment.LiqPayPayment;
import org.springframework.beans.factory.annotation.Value;
import restaurant.petproject.service.impl.ShopServiceImpl;


import java.util.Map;

@Controller
public class LiqPayController {
    @Value("${liqpay.public-key}")
    private String publicKey;

    @Value("${liqpay.private-key}")
    private String privateKey;

    private final ShopServiceImpl shopService;
    @Autowired
    public LiqPayController(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/liqpay-callback")
    public String handleLiqPayCallback(@RequestParam Map<String, String> params, Model model) {
        LiqPayCallbackHandler callbackHandler = new LiqPayCallbackHandler(privateKey);
        callbackHandler.handleCallBack(params);

        // Обработка результата и передача в модель для отображения
        model.addAttribute("result", params);
        return "payment-result";
    }

    @GetMapping("/payment")
    public String paymentForm(HttpSession session, Authentication auth, Model model) {
        ShoppingCart shop = shopService.getShoppingCartByUser((User) auth.getPrincipal());
        if (shop == null || shop.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        LiqPayPayment liqPayPayment = new LiqPayPayment(publicKey, privateKey);
        String amount = String.valueOf(shop.getTotalPrice());
        String currency = "UAH";
        String description = "Payment for order " + shop.getId();
        String orderId = "order_id_" + System.currentTimeMillis();

        String serverUrl = "https://c2f6-159-224-20-62.ngrok-free.app/liqpay-callback";

        String paymentFormUrl = liqPayPayment.createPayment(amount, currency, description, orderId, serverUrl);

        model.addAttribute("paymentFormUrl", paymentFormUrl);
        return "auto-submit-payment-form";
    }

}
