package restaurant.petproject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import restaurant.petproject.entity.Dish;
import restaurant.petproject.entity.Image;
import restaurant.petproject.entity.User;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.ImageService;
import restaurant.petproject.service.impl.DishServiceImpl;
import restaurant.petproject.service.impl.ImageServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/User")
public class DishController {
    private final ShopServiceImpl shopService;
    private DishRepository dishRepository;
    private final DishServiceImpl dishService;
    private ImageServiceImpl imageService;
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
    public String menuMain(Principal principal, Model model){
        Iterable<Dish> dishes = dishRepository.findAll();
        model.addAttribute("dishes", dishes);

        String Email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(Email);

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("shop", shopService.getShoppingCartByUser(user));
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("shop", null);
        }
        return "menu";
    }

    @GetMapping("/authorization")
    public String authorizationGet(Model model) {
        return "authorization";
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException
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
