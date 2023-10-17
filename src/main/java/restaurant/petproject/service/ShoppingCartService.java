package restaurant.petproject.service;

import org.springframework.stereotype.Service;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.ShoppingCart;

@Service
public interface ShoppingCartService {
    ShoppingCart addShoppingCartFirstTime(Long id, String sessionToken, int quantity);
    ShoppingCart addToExistingShoppingCart(Long id, String sessionToken, int quantity);
    ShoppingCart getShoppingCartBySessionToken(String sessionToken);
    CartItem updateShoppingCartItem(Long id, int quantity);
    ShoppingCart removeCartItemFromShoppingCart(Long id, String sessionToken);
    void clearShoppingCart(String sessionToken);


}
