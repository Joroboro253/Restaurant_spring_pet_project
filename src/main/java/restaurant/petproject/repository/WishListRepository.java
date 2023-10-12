package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList,Long> {
    WishList findBySessionToken(String sessionToken);
}
