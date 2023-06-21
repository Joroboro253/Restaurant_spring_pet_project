package restaurant.petproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import restaurant.petproject.models.Dish;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.DishService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DishController {
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private final DishService dishService;

//    public DishController(DishService dishService) {
//        this.dishService = dishService;
//
//    }


    @GetMapping("/menu")
    public String menuMain(String title, Principal principal, Model model){
        Iterable<Dish> dishs = dishRepository.findAll();
        model.addAttribute("dish", dishs);
        return "menu";
    }

    @GetMapping("/dish/add")
    public String dishAdd(Model model) {
        return "dish-add";
    }

    @PostMapping("/dish/add")
    public String dishPostAdd(@RequestParam String title, @RequestParam String description, @RequestParam int price, Model model){
//        Dish dish = new Dish(title, description, price);
//        dishRepository.save(dish);
        return "redirect:/menu";
//        return "/menu";
    }

    //будет ли работать
//    @GetMapping("/dish/{id}")
//    public String dishDetails(@PathVariable(value = "id") long id, Model model) {
//        if(!dishRepository.existsById(id)) {
//            return "redirect:/menu";
//        }
//
//        Optional<Dish> dish = dishRepository.findById(id);
//        ArrayList<Dish> res = new ArrayList<>();
//        dish.ifPresent(res::add);
//        model.addAttribute("dish", res);
//        return "dish-details";
//    }
    @GetMapping("/product/{id}")
    public String dishInfo(@PathVariable Long id, Model model, Principal principal) {
        Dish dish = dishService.getDishById(id);
        model.addAttribute("user", dishService.getUserByPrincipal(principal));
        model.addAttribute("dish", dish);
        model.addAttribute("images", dish.getImages());
        model.addAttribute("authorDish", dish.getUser());
        return "dish-info";
    }

    @GetMapping("/dish/{id}/edit")
    public String dishEdit(@PathVariable(value = "id") long id, Model model){
        if(!dishRepository.existsById(id)) {
            return "redirect:/menu";
        }
        Optional<Dish> dish = dishRepository.findById(id);
        ArrayList<Dish> res = new ArrayList<>();
        dish.ifPresent(res::add);
        model.addAttribute("dish", res);
        return "dish-edit";
    }

    @PostMapping("/dish/{id}/edit")
    public String dishPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String content, Model model){
        Dish dish = dishRepository.findById(id).orElseThrow();
        dish.setTitle(title);
        dish.setDescription(content);
        dishRepository.save(dish);

        return "redirect:/menu";
    }

    @PostMapping("/dish/{id}/remove")
    public String dishPostDelete(@PathVariable(value = "id") long id, Model model) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        dishRepository.delete(dish);

        return "redirect:/menu";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

  //  @GetMapping()
    //new

    @GetMapping("/")
    public String dishes(@RequestParam(name = "searchWord", required = false) String title, Principal principal, Model model) {
        model.addAttribute("dishes", dishService.listDishes(title));
        model.addAttribute("user", dishService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "dishes";
    }

}
