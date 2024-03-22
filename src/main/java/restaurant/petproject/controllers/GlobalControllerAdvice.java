package restaurant.petproject.controllers;

import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import restaurant.petproject.entity.User;
import restaurant.petproject.service.UserService;
import restaurant.petproject.service.impl.UserServiceImpl;

// Благодаря данной аннотации код автоматически применится к контроллерам
@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserServiceImpl userService;
    @ModelAttribute("users")
    public User globalUser() {
        return userService.getCurrentUser();
    }
}
