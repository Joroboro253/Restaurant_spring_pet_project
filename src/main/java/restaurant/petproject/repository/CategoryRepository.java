package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.Carousel;
import restaurant.petproject.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
