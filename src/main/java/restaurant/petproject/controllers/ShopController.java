package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import restaurant.petproject.entity.ShoppingCart;
import restaurant.petproject.entity.User;
import restaurant.petproject.service.impl.DishServiceImpl;
import restaurant.petproject.service.impl.ShopServiceImpl;
import restaurant.petproject.service.impl.UserServiceImpl;

import java.util.Optional;

@Controller
//@RequestMapping("User")
public class ShopController {
    private final ShopServiceImpl shopService;
    private final UserServiceImpl userService;
    private final DishServiceImpl dishService;
    @Autowired
    public ShopController(ShopServiceImpl shopService, UserServiceImpl userService, DishServiceImpl dishService) {
        this.shopService = shopService;
        this.userService = userService;
        this.dishService = dishService;
    }

    @GetMapping("/cart")
    public String cartView(Model model, @RequestParam("id") Optional<Long> id, Authentication auth){
        if(id.isPresent()){
            model.addAttribute("shop", shopService.getShoppingCartById(id.get()));
        }else{
            model.addAttribute("shop", shopService.creatEmptyCart((User) auth.getPrincipal()));
        }

        model.addAttribute("user", userService.findByEmail(auth.getName()));
        return "cart";
    }

    @GetMapping("/add-cart")
    public String addToCart(@RequestParam("id") Long id) {
        shopService.addItem(id);
        return "redirect:/index";
    }
    @GetMapping("/remove-cartItem/{id}")
    public String addToCart(Model model, Authentication auth, @PathVariable("id") Long id) {
        ShoppingCart shop = shopService.removeItem((User) auth.getPrincipal(),id);
        model.addAttribute("shop", shop);
        model.addAttribute("user", userService.findByEmail(((User) ((User) auth.getPrincipal())).getUsername()));
        return "cart";
    }

    @GetMapping("/update-cartItem")
    public String updateItemQuantityCart(Model model, Authentication auth, @RequestParam("id") Long id,
                                         @RequestParam("quantity") Integer quantity ) {
        ShoppingCart shop = shopService.updateItemQuantity((User) auth.getPrincipal(),id,quantity);
        model.addAttribute("shop", shop);
        System.out.println( ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
        model.addAttribute("user", userService.findByEmail(((User) ((User) auth.getPrincipal())).getUsername()));
        return "cart";
    }
}
