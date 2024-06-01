package restaurant.petproject.payment;

import com.liqpay.LiqPay;

import java.util.HashMap;
import java.util.Map;

public class LiqPayPayment {
    private final LiqPay liqPay;

    public LiqPayPayment(String publicKey, String privateKey) {
        this.liqPay = new LiqPay(publicKey, privateKey);
    }

    public String createPayment(String amount, String currency, String description, String orderId, String serverUrl){
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("description", description);
        params.put("order_id", orderId);
        params.put("version", "3");
        params.put("sandbox", "1"); // 1 for testing, 0 for real payment
        params.put("servet_url", serverUrl);

        return liqPay.cnb_form(params);
    }
}
