package restaurant.petproject.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import restaurant.petproject.dto.UserDto;
import restaurant.petproject.entity.UserEntity;
import restaurant.petproject.service.UserService;

import java.util.List;


@Controller
public class AuthController {
    @Autowired
    private UserService userService;



    public AuthController(UserService userService) {
        this.userService = userService;
    }
    public AuthController() {
    }

    //    При создании без параметров возникает ошибка
//    public AuthController() {
//    }

    // Здесь должен быть код для обработки начальной страницы, но он уже есть в классе MainController
    @GetMapping("index")
    public String home(){
        return "index";
    }



    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }



    //handler method t handle user registration from request
    @GetMapping("register")
    public String showRegistrationFrom(Model model){
        //create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }



    //handler method to handle user registration from submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){
        UserEntity existingUser = userService.findByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null, "There is already registered with" +
                    "the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }



}
