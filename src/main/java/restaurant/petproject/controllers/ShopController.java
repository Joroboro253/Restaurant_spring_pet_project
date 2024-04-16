package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import restaurant.petproject.service.impl.DishServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;

@Controller
@RequestMapping("User")
public class ShopController {
    private final ShopServiceImpl shopService;
    private final UserServiceImpl userService;
    private final DishServiceImpl dishService;
    @Autowired
    public ShopController(ShopServiceImpl shopService, UserServiceImpl userService, DishServiceImpl dishService) {
        this.shopService = shopService;
        this.userService = userService;
        this.dishService = dishService;
    }


}
