package restaurant.petproject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequiredArgsConstructor
public class DishController {
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private final DishServiceImpl dishService;
    @Autowired
    private ImageServiceImpl imageService;

    @GetMapping("/menu")
    public String menuMain(Principal principal, Model model){
        Iterable<Dish> dishes = dishRepository.findAll();
        Optional<User> user = dishService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("dish", dishes);
        return "menu";
    }

    @GetMapping("/authorization")
    public String authorizationGet(Model model) {
        return "authorization";
    }

    @GetMapping("/dish/add")
    public ModelAndView addImage() {
        return new ModelAndView("dish-add");
    }

    //    @PostMapping("/dish/add")
//    public String dishPostAdd(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, Dish dish, Principal principal) throws IOException {
//        dishService.saveDish(principal, dish, file1, file2);
//        return "redirect:/menu";
//    }


    @PostMapping("/dish/add")
    public String addImagePost(@RequestParam("image")MultipartFile files, Dish dish, Principal principal) throws IOException, SerialException, SQLException {
//        List<Image> images = imageService.fromFileToImage(files);
        Dish updatedDish = dishService.updateDishImages(dish, files);
//        dishRepository.save(dish);
//        зменить обратнго
//        dishService.saveDish(principal, dish, images);
        dishService.saveDish(principal, updatedDish);
        return "redirect:/menu";
    }

//    @GetMapping("/display")
//    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException
//    {
//        Image image = imageService.viewById(id);
//        byte [] imageBytes = null;
//        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
//    }
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
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
// Решаем позже
//        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());

        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        model.addAttribute("image", encodedImage);
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
    public String dishPostUpdate(@PathVariable(value = "id") long id,  @RequestParam("image")MultipartFile files, Principal principal, @RequestParam String title, @RequestParam String description, @RequestParam int price) throws IOException, SQLException {
        Dish currentDish = new Dish(title, description, price);
            Dish updatedDish = dishService.updateDishImages(currentDish, files);
//        dishRepository.save(dish);
//        зменить обратнго
//        dishService.saveDish(principal, dish, images);
        dishService.saveDish(principal, updatedDish);
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
