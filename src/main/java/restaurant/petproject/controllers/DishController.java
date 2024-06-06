package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import restaurant.petproject.entity.Dish;
import restaurant.petproject.entity.Image;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.impl.DishServiceImpl;
import restaurant.petproject.service.impl.ImageServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;
import org.springframework.security.core.Authentication;


import java.security.Principal;
import java.sql.SQLException;
import java.util.Base64;

@Controller
public class DishController {
    private final ShopServiceImpl shopService;
    private final DishRepository dishRepository;
    private final DishServiceImpl dishService;
    private final ImageServiceImpl imageService;
    private UserServiceImpl userService;
    @Autowired
    public DishController(ShopServiceImpl shopService, DishRepository dishRepository, DishServiceImpl dishService, ImageServiceImpl imageService, UserServiceImpl userService) {
        this.shopService = shopService;
        this.dishRepository = dishRepository;
        this.dishService = dishService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/menu")
    public String menuMain(Model model, User user){
        Iterable<Dish> dish = dishRepository.findAll();
        model.addAttribute("dish", dish);
        model.addAttribute("user", user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user2 = userService.findByEmail(email);

            if (user2 != null) {
                model.addAttribute("user", user2);
                model.addAttribute("shop", shopService.getShoppingCartByUser(user));
            } else {
                model.addAttribute("user", new User());
                model.addAttribute("shop", new ShoppingCart());
            }
        }  else {
            model.addAttribute("user", new User());
            model.addAttribute("shop", new ShoppingCart());
        }
        return "menu";
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws SQLException
    {
        Image image = imageService.viewById(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @GetMapping("/dish/{id}")
    public String dishInfo(@PathVariable Long id, Model model) throws SQLException {
        Dish dish = dishService.getDishById(id);

        model.addAttribute("dish", dish);
        model.addAttribute("images", dish.getImages());
        model.addAttribute("authorDish", dish.getUser());
        Image image = imageService.viewById(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());

        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        model.addAttribute("image", encodedImage);
        return "dish-info";
    }

    


}
