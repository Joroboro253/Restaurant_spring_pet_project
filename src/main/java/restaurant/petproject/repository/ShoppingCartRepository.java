package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findBySessionToken(String sessionToken);
    ShoppingCart findByUser(User User);
    void deleteByUserId(Long userId);
}
