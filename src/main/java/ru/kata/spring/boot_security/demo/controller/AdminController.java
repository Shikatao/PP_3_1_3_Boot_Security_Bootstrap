package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAllUsers(Model model) {

        model.addAttribute("allUsers", userService.getAllUsers());
        return "adminPanel";
    }


    //    @RequestMapping("/admin/addNewUser")
    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "add";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam String[] rolesi) {

        Set<Role> setRoles = new HashSet<>();
        for (String s : rolesi) {
            setRoles.add(userService.findByRole(s));
        }
        user.setRoles(setRoles);
        userService.saveUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/updateInfo")
    public String updateInfo(@RequestParam("userId") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "edit";
    }


    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam String[] rolesi) {
        Set<Role> setRoles = new HashSet<>();
        for (String s : rolesi) {
            setRoles.add(userService.findByRole(s));
        }
        user.setRoles(setRoles);
        userService.updateUser(user);

        return "redirect:/admin";
    }


    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}
