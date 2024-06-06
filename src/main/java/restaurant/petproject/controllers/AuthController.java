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
import restaurant.petproject.entity.User;
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

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationFrom(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){
        User existingUser = userService.findByEmail(userDto.getEmail());

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
}
