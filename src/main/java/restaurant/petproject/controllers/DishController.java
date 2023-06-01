package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import restaurant.petproject.models.Dish;
import restaurant.petproject.repository.DishRepository;

@Controller
public class DishController {
    @Autowired
    private DishRepository dishRepository;

    @GetMapping("/menu")
    public String menuMain(Model model){
        Iterable<Dish> dishs = dishRepository.findAll();
        model.addAttribute("dish", dishs);
        return "menu";
    }



    @PostMapping("/dish/add")
    public String nodePostAdd(@RequestParam String title, @RequestParam String description, int price){
        Dish dish = new Dish(title, description, price);
        dishRepository.save(dish);
        return "redirect:/menu";
//        return "/menu";
    }



    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

  //  @GetMapping()

}
