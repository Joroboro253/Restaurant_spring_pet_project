package restaurant.petproject.repository;

import org.springframework.data.repository.CrudRepository;
import restaurant.petproject.models.Dish;


// Это место для хранения блюд
public interface DishRepository extends CrudRepository<Dish, Long> {
}
