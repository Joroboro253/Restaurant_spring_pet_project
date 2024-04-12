package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import restaurant.petproject.entity.User;
import restaurant.petproject.service.impl.ShopServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;

// Благодаря данной аннотации код автоматически применится к контроллерам
@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ShopServiceImpl shopService;

    @ModelAttribute("users")
    public User globalUser(Model model) {
        String Email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(Email);

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("shop", shopService.getShoppingCartByUser(user));
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("shop", null);
        }

        return userService.getCurrentUser();


    }
}
