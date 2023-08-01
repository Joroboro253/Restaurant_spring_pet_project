package restaurant.petproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import restaurant.petproject.service.UserService;
import restaurant.petproject.service.impl.UserServiceImpl;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
}