package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.WishListItem;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {
}
