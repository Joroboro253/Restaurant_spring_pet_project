package restaurant.petproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import restaurant.petproject.repo.MenuRepository;

@Controller
public class MenuController {
    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/menu")
    public String menuMain(Model model){


        return "menu";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }


}
