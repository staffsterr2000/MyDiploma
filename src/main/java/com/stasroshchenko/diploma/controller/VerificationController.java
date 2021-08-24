package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.request.RegistrationRequest;
import com.stasroshchenko.diploma.model.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Works with Verification page, all the requests at
 * "localhost:8080/verification" will be proceeded in this
 * controller.
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
@RequestMapping("/verification")
@AllArgsConstructor
public class VerificationController {

    /**
     * Connection with registration service
     */
    private final RegistrationService registrationService;



    /**
     * On GET request returns Verification view.
     * @return View name
     * @param request Redirected from Registration page, registration request
     * @since 1.0
     */
    @GetMapping
    public String getVerificationView(
            @ModelAttribute("redirectedRequest")
                    RegistrationRequest request) {

        return "verification";
    }



    /**
     * If request has no validation and binding errors, the method tries to pass
     * request to registration service. Saves request for successful redirection
     * and redirects to the Verification page.
     * @param request The filled-by-user registration request, which is validated
     * @param result Result of validation and binding (contains errors)
     * @param redirectAttributes Attributes that will be redirected to next view
     * @return Link where user will be redirected
     * @since 1.0
     * @see RegistrationService
     */
    @PostMapping("/resend")
    public String resendConfirmationToken(
            @ModelAttribute RegistrationRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            return "registration";
        }
        try {
            registrationService.resendConfirmationToken(request);
        } catch (IllegalStateException ex) {
            result.addError(
                    new ObjectError("error", ex.getMessage())
            );
            return "registration";
        }

        redirectAttributes.addFlashAttribute("redirectedRequest", request);

        return "redirect:/verification";
    }

}
