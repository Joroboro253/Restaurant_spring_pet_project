package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.Carousel;

public interface CarouselRepository extends JpaRepository<Carousel, Long> {
}
