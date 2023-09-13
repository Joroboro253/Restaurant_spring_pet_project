package restaurant.petproject.service;

import org.springframework.stereotype.Service;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.ShoppingCart;

@Service
public interface ShoppingCartService {
    public ShoppingCart addShoppingCartFirstTime(Long id, String sessionToken, int quantity);
    public ShoppingCart addToExistingShoppingCart(Long id, String sessionToken, int quantity);
    public ShoppingCart getShoppingCartBySessionToken(String sessionToken);
    public CartItem updateShoppingCartItem(Long id, int quantity);
    public ShoppingCart removeCartItemFromShoppingCart(Long id, String sessionToken);
    public void clearShoppingCart(String sessionToken);


}
