package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.model.service.RegistrationService;
import com.stasroshchenko.diploma.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Works with Registration page, all the requests at
 * "localhost:8080/registration" are proceeded in this controller
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    /**
     * Connection with registration service
     */
    private final RegistrationService registrationService;



    /**
     * Runs on GET request, creates new empty registration request
     * object, that will be filled during user is filling and sends
     * form back to server as one object.
     * Adds the created registration request to Model.
     * @param model Model of attributes
     * @return View name
     * @since 1.0
     */
    @GetMapping
    public String getRegistrationView(Model model) {
        model.addAttribute("request", new RegistrationRequest());

        return "registration";
    }



    /**
     * Runs on POST request, if result has errors, sends view back.
     * Otherwise, tries to send the request to the service and then redirect
     * the user to Verification page.
     * @param request The filled-by-user registration request, which is validated
     * @param result Result of validation and binding (contains errors)
     * @param redirectAttributes Attributes that will be redirected to next view
     * @return Link where user will be redirected
     * @since 1.0
     * @see RegistrationService
     */
    @PostMapping
    public String signUpUser(
            @Valid @ModelAttribute("request") RegistrationRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            return "registration";
        }
        try {
            registrationService.signUpUser(request);
        } catch (IllegalStateException ex) {
            result.addError(
                    new ObjectError("error", ex.getMessage())
            );
            return "registration";
        }

        redirectAttributes.addFlashAttribute("redirectedRequest", request);

        return "redirect:/verification";
    }



    /**
     * Passes token to service and redirects to Login page.
     * @param token Token to confirm
     * @return Link where user will be redirected
     * @since 1.0
     * @see RegistrationService
     */
    @GetMapping("/confirm")
    public String confirmToken(
            @RequestParam("token") String token) {

        registrationService.confirmToken(token);

        return "redirect:/login";
    }

}
