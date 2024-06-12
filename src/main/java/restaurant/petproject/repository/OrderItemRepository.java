package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

