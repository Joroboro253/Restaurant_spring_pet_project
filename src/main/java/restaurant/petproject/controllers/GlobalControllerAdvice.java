package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import restaurant.petproject.entity.ShoppingCart;
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

    @ModelAttribute
    public void globalUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("shop", shopService.getShoppingCartByUser(user));
            } else {
                model.addAttribute("user", new User());
                model.addAttribute("shop", new ShoppingCart());
            }
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("shop", new ShoppingCart());
        }

//        return userService.getCurrentUser();
    }
}
