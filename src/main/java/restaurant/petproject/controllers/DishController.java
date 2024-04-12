package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import restaurant.petproject.entity.Dish;
import restaurant.petproject.entity.Image;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.impl.DishServiceImpl;
import restaurant.petproject.service.impl.ImageServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;

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

    @GetMapping("/menu")
    public String menuMain(Model model){
        Iterable<Dish> dishes = dishRepository.findAll();
        model.addAttribute("dishes", dishes);

        return "menu";
    }

    @GetMapping("/authorization")
    public String authorizationGet() {
        return "authorization";
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws SQLException
    {
        Image image = imageService.viewById(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/dish/{id}")
    public String dishInfo(@PathVariable Long id, Model model, Principal principal) throws SQLException {
        Dish dish = dishService.getDishById(id);
        model.addAttribute("user", dishService.getUserByPrincipal(principal));
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
