package restaurant.petproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import restaurant.petproject.dto.UserDto;
import restaurant.petproject.entity.Dish;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.service.impl.DishServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserServiceImpl userService;
    @Autowired
    private final DishServiceImpl dishService;
    @Autowired
    private DishRepository dishRepository;
    @GetMapping("/dish/add")
    public ModelAndView addImage() {
        return new ModelAndView("dish-add");
    }

    @PostMapping("/dish/add")
    public String addImagePost(@RequestParam("image")MultipartFile[] files, Dish dish, Principal principal) throws IOException, SerialException, SQLException {
        Dish updatedDish = dishService.updateDishImages(dish, files);
        dishService.saveDish(principal, updatedDish);
        return "redirect:/menu";
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
    public String dishPostUpdate(@PathVariable(value = "id") long id,  @RequestParam("image")MultipartFile[] files, Principal principal, @RequestParam String title, @RequestParam String description, @RequestParam int price) throws IOException, SQLException {
        Optional<Dish> dishOptional = dishRepository.findById(id);
        if (dishOptional.isPresent()) {
            Dish dishToUpdate = dishOptional.get();
            dishToUpdate.setTitle(title);
            dishToUpdate.setDescription(description);
            dishToUpdate.setPrice(price);

            if(files != null) {
                dishToUpdate = dishService.updateDishImages(dishToUpdate, files);
            }
            dishRepository.save(dishToUpdate);
        } else {
            return "redirect:/menu";
        }

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
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }



}
