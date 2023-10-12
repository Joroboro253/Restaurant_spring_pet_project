package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.petproject.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT DISTINCT c FROM Coupon c where c.discount=(SELECT MAX(discount))")
    Coupon findMax();
}
