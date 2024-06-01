package restaurant.petproject.payment;

import com.liqpay.LiqPay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

public class LiqPayCallbackHandler {
    private final String privateKey;

    public LiqPayCallbackHandler(String privateKey) {
        this.privateKey = privateKey;
    }

    public void handleCallBack(Map<String, String> params) {
        String data = params.get("data");
        String signature = params.get("signature");

        boolean isValid = checkSignature(data, signature);
        if (isValid) {
            System.out.println("Valid callback: " + data);
        } else {
            System.out.println("Invalid callback");
        }
    }

    private boolean checkSignature(String data, String signature) {
        String generatedSignature = generateSignature(data);
        return generatedSignature.equals(signature);
    }

    private String generateSignature(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            String text = privateKey + data + privateKey;
            messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

}
