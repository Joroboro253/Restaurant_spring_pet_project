package restaurant.petproject.repo;

import org.springframework.data.repository.CrudRepository;
import restaurant.petproject.models.Dish;

public interface MenuRepository extends CrudRepository<Dish, Long> {
}
