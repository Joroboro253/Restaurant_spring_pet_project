package restaurant.petproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restaurant.petproject.entity.UserEntity;
import restaurant.petproject.models.Dish;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.DishService;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    public final DishRepository dishRepository;
    private final UserRepository userRepository;

    public List<Dish> listDishes(String title) {
        if(title != null) return dishRepository.findByTitle(title);
        return dishRepository.findAll();
    }

    public void saveDish(Principal principal, Dish dish) throws Exception {
        dish.setUser(getUserByPrincipal(principal));
    }

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    public UserEntity getUserByPrincipal(Principal principal) {
        if(principal == null) return new UserEntity();
        return userRepository.findByEmail(principal.getName());

    }
}
