package restaurant.petproject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
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

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ShopServiceImpl shopService;
//    private Logger log;

    @ModelAttribute
    public void globalUser(Model model, HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        if(requestURI.equals("login") || requestURI.equals("login/") || requestURI.equals("/register/save")) {
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user != null) {
                ShoppingCart cart = shopService.getShoppingCartByUser(user);
                model.addAttribute("user", user);
                model.addAttribute("shop", cart);
//                log.info("Authentication object: " + authentication);
//                log.info("User object: " + user);
//                log.info("ShoppingCart object: " + cart);
            } else {
                model.addAttribute("user", new User());
                model.addAttribute("shop", new ShoppingCart());
            }
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("shop", new ShoppingCart());
        }
    }
}
