package restaurant.petproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.UserEntity;
import restaurant.petproject.models.Dish;
import restaurant.petproject.models.Image;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.DishService;

import java.io.IOException;
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

    @GetMapping("/menu")
    public String menuMain(Model model){
        Iterable<Dish> dishes = dishRepository.findAll();
        model.addAttribute("dish", dishes);
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
        if(!dishRepository.existsById(id)) {
            return "redirect:/dish";
        }
        Optional<Dish> dish = dishRepository.findById(id);
        ArrayList<Dish> res = new ArrayList<>();
//        model.addAttribute("user", dishService.getUserByPrincipal(principal));
//        model.addAttribute("dish", dish);
        dish.ifPresent(res::add);
        model.addAttribute("dish", res);
        return "dish-info";
    }

    @GetMapping("/dish/{id}/edit")
    public String dishEdit(@PathVariable(value = "id") long id, Model model) {
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
    public String dishPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String description, @RequestParam int price, Model model) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        dish.setTitle(title);
        dish.setDescription(description);
        dish.setPrice(price);
        dishRepository.save(dish);

        return "redirect:/menu";
    }


    @PostMapping("/dish/{id}/remove")
    public String dishPostDelete(@PathVariable(value = "id") long id, Model model) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        dishRepository.delete(dish);

        return "redirect:/menu";
    }

    @GetMapping("/dish/{id}/remove")
    public String dishGetDelete(@PathVariable(value = "id") long id, Model model){
        return "/menu";
    }






}
