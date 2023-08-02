package restaurant.petproject.service;

import restaurant.petproject.entity.Dish;

import java.security.Principal;

public interface DishService {
    Dish getDishById(Long id);
    Object getUserByPrincipal(Principal principal);
}
