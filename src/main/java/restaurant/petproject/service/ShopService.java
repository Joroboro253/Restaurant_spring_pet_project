package restaurant.petproject.service;

import org.springframework.stereotype.Service;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;
import restaurant.petproject.repository.CartItemRepository;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.ShoppingCartRepository;
import restaurant.petproject.repository.UserRepository;

import java.util.Set;

@Service
public interface ShopService {
    void addItem(Long product_id, Integer quantity);
    boolean itemIsNotThere(Set<CartItem> items, Long product_id);
    ShoppingCart removeItem(User user, Long item_id);
    ShoppingCart updateItemQuantity(User user, Long item_id, Integer newQuantity);
    void deleteShoppingCart(User user);
    ShoppingCart getShoppingCartByUser(User user);
    ShoppingCart getShoppingCartById(Long id);
    ShoppingCart creatEmptyCart(User principal);
}
