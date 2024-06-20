package restaurant.petproject.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import restaurant.petproject.entity.CartItem;

import restaurant.petproject.entity.Dish;
import restaurant.petproject.entity.User;
import restaurant.petproject.repository.CartItemRepository;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.ShoppingCartRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.ShopService;
import restaurant.petproject.entity.ShoppingCart;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    @Autowired
    public ShopServiceImpl(CartItemRepository cartItemRepository, DishRepository productRepository, ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.dishRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void addItem(Long dish_id, Integer quantity  ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if(shoppingCart != null){
            if(itemIsNotThere(shoppingCart.getItems(), dish_id)){
                CartItem item = new CartItem(dishRepository.getById(dish_id),quantity);
                shoppingCart.getItems().add(item);
                shoppingCartRepository.save(shoppingCart);
            } else {

                for (CartItem it : shoppingCart.getItems()) {
                    if(it.getDish().getId().equals(dish_id)){
                        it.setQuantity(it.getQuantity()+quantity);
                        cartItemRepository.save(it);
                    }
                }
            }
        } else {
            ShoppingCart shopping = new ShoppingCart();
            shopping.setUser(user);
            CartItem item = new CartItem(dishRepository.getById(dish_id),quantity);
            cartItemRepository.save(item);
            shopping.getItems().add(item);
            shoppingCartRepository.save(shopping);
        }

    }


    public boolean itemIsNotThere(Set<CartItem> items, Long dish_id) {
       for (CartItem item : items) {
           if(item.getDish().getId().equals(dish_id)){
               return false;
           }
       }
       return true;
    }

//    public ShoppingCart removeItem(User user, Long item_id) {
//        ShoppingCart shoppingCart = getShoppingCartByUser(user);
//        Set<CartItem> items = shoppingCart.getItems().stream().filter(item -> !item.getId().equals(item_id)).collect(Collectors.toSet());
//        shoppingCart.setItems(items);
//        cartItemRepository.deleteById(item_id);
//        return shoppingCartRepository.save(shoppingCart);
//    }
    public ShoppingCart removeItem(User user, Long cartItemId) {
        ShoppingCart cart = shoppingCartRepository.findByUser(user);
        cartItemRepository.deleteById(cartItemId);
        return cart;
    }

    public ShoppingCart updateItemQuantity(User user, Long item_id, Integer newQuantity) {
        ShoppingCart shoppingCart = getShoppingCartByUser(user);
        for (CartItem item : shoppingCart.getItems()) {
            if(item.getId().equals(item_id)){
                item.setQuantity(newQuantity);
                cartItemRepository.save(item);
                break;
            }
        }
        return getShoppingCartByUser(user);
    }

    public void deleteShoppingCart(User user) {
        ShoppingCart shoppingCart = getShoppingCartByUser(user);
        shoppingCartRepository.delete(shoppingCart);
    }

    public ShoppingCart getShoppingCartByUser(User user) {
        User account = userRepository.findByEmail(user.getUsername());
        ShoppingCart cart = shoppingCartRepository.findByUser(account);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setUser(account);
            shoppingCartRepository.save(cart); // saving a new cart
        }
        return cart;
    }

    public ShoppingCart getShoppingCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElse(new ShoppingCart());
    }

    public ShoppingCart creatEmptyCart(User principal) {
        User user = userRepository.findByEmail(principal.getUsername());
        if(shoppingCartRepository.findByUser(user) == null) {
            ShoppingCart shope = new ShoppingCart();
            shope.setUser(user);
            return shoppingCartRepository.save(shope);
        } else {
            return shoppingCartRepository.findByUser(user);
        }
    }
    @Transactional
    public void clearCart(User user) {
        ShoppingCart cart = shoppingCartRepository.findByUser(user);
        if (cart != null) {
            shoppingCartRepository.delete(cart);
        }
    }

    // Delete Linked Records in the CartItem Repository
    public void deleteDish(Long dishId) {
        List<CartItem> cartItems = cartItemRepository.findByDishId(dishId);
        cartItemRepository.deleteAll(cartItems);
        dishRepository.deleteById(dishId);
    }


    public void save(ShoppingCart cart) {
        shoppingCartRepository.save(cart);
    }

}
