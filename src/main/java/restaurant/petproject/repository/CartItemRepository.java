package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurant.petproject.entity.Carousel;
import restaurant.petproject.entity.CartItem;
import restaurant.petproject.entity.Category;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
