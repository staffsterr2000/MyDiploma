package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.request.RegistrationRequest;
import com.stasroshchenko.diploma.model.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

// контролер який працює з посиланням host:port/registration
@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    // зв'язок з сервісом реєстрації
    private final RegistrationService registrationService;

    // виконується при надходженні запиту з методом GET
    // за посиланням host:port/registration
    @GetMapping
    public String getRegistrationView(Model model) {

        // додає до моделі атрибутів новий реквест
        // для подальшого заповнення
        model.addAttribute("request", new RegistrationRequest());

        // повертає Шаблон registration.html
        return "registration";
    }

    // виконується при надходженні запиту з методом POST
    // за посиланням host:port/registration
    @PostMapping
    public String signUpUser(
            // надходять:
            // 1. заповнений реквест, який одразуж валідується
            @Valid @ModelAttribute("request") RegistrationRequest request,
            // 2. модель помилок
            BindingResult result,
            // 3. модель для пересилання атрибутів
            RedirectAttributes redirectAttributes
    ) {

        // якщо є помилки у валідації, то повертаємо Шаблон
        // у який додаються помилки пов'язані з валідацією
        if(result.hasErrors()) {
            return "registration";
        }

        // намагаємося зареєструвати користувача
        try {
            registrationService.signUpUser(request);
        } catch (IllegalStateException ex) {
            // якщо отримуємо ексепшн, то додаємо його до моделі помилок
            result.addError(new ObjectError("error", ex.getMessage()));
            // та повертаємо Шаблон registration.html
            return "registration";
        }

        // прокидуємо реквест на сторінку верифікації
        redirectAttributes.addFlashAttribute("redirectedRequest", request);
        return "redirect:/verification";
    }

    @GetMapping("/confirm")
    public String confirmToken(
            @RequestParam("token") String token) {

        registrationService.confirmToken(token);
        return "redirect:/login";
    }

}
