package restaurant.petproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import restaurant.petproject.models.Dish;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.DishService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DishController {
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private final DishService dishService;

    @GetMapping("/menu")
    public String menuMain(Model model){
        Iterable<Dish> dishs = dishRepository.findAll();
        model.addAttribute("dish", dishs);
        return "menu";
    }



    @GetMapping("/authorization")
    public String authorizationGet(Model model) {
        return "authorization";
    }


    @PostMapping("/dish/add")
    public String nodePostAdd(@RequestParam String title, @RequestParam String description, int price){
        Dish dish = new Dish(title, description, price);
        dishRepository.save(dish);
        return "redirect:/menu";
//        return "/menu";
    }

    @GetMapping("/dish/add")
    public String noteGetAdd(Model model) {
        return "dish-add";
    }


    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

    @GetMapping("/dish/{id}")
    public String dishDetails(@PathVariable Long id, Model model, Principal principal) {
        Dish dish = dishService.getDishById(id);
//        model.addAttribute("user", dishService.getUserByPrinipal(principal));
        model.addAttribute("user", dishService.getUserByPrincipal(principal));
        model.addAttribute("dish", dish);
        return "dish-info";
    }

  //  @GetMapping()

}
