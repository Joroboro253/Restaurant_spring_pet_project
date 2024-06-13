package restaurant.petproject.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.liqpay.LiqPay;

import java.util.HashMap;
import java.util.Map;

@Component
public class LiqPayPayment {
    private final LiqPay liqPay;

    @Value("${liqpay.public-key}")
    private String publicKey;

    @Value("${liqpay.private-key}")
    private String privateKey;

    public LiqPayPayment(@Value("${liqpay.public-key}") String publicKey, @Value("${liqpay.private-key}") String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.liqPay = new LiqPay(publicKey, privateKey);
    }

    public String createPayment(String amount, String currency, String description, String orderId, String serverUrl, String returnUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("version", "3");
        params.put("public_key", publicKey);
        params.put("action", "pay");
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("description", description);
        params.put("order_id", orderId);
        params.put("server_url", serverUrl);
        params.put("result_url", returnUrl);

        return liqPay.cnb_form(params);
    }
}

