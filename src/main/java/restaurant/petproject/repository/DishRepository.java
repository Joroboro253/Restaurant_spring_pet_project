package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.Dish;

import java.util.List;


// Это место для хранения блюд
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByTitle(String title);
}
