package restaurant.petproject.repository;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import restaurant.petproject.models.Dish;

import java.util.List;


// Это место для хранения блюд
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByTitle(String title);
}
