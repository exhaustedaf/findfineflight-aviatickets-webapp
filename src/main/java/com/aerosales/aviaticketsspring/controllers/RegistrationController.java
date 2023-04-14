package com.aerosales.aviaticketsspring.controllers;

import com.aerosales.aviaticketsspring.pojo.Users;
import com.aerosales.aviaticketsspring.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Users user, Model model) {
        Users userFromDb = usersRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "Пользователь существует!");
            return "registration";
        }
        user.setRole("user");
        usersRepository.save(user);
        return "redirect:/login";
    }
}
