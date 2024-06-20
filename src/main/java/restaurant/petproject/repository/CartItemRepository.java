package restaurant.petproject.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import restaurant.petproject.entity.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByDishId(Long dishId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.dish.id = :dishId")
    void deleteByDishId(@Param("dishId") Long dishId);
}
