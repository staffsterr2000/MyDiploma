package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// цей контролер працює з головною
// всі реквести по посиланню host:port/ будуть надходити у цей контролер
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    // зв'язок з сервісом юзерів
    private final ApplicationUserService applicationUserService;

    // за допомогою шару Моделі
    // додає до моделі атрибутів всіх докторів-користувачів
    @ModelAttribute("allDoctorUsers")
    public List<ApplicationUserDoctor> getAllDoctorUsers() {
        return applicationUserService.getAllUserDoctors();
    }

    // ця функція виконується при надходженні
    // реквесту на посилання host:port/ за методом GET
    @GetMapping
    public String getIndexView(Model model) {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // також додає до моделі атрибутів
        // поточну автентифікацію користувача
        model.addAttribute("auth", authentication);

        // повертає Шаблон "index.html"
        return "index";
    }

}
