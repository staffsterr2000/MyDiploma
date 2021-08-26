package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.security.ApplicationSecurityConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Works with Login page, all the requests that income either
 * to "localhost:8080/login" or to "localhost:8080/login-error"
 * are passed to this controller.
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
public class LoginController {

    /**
     * On GET request returns login view.
     * @return view name
     * @since 1.0
     */
    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }



    /**
     * On login failure returns extended Model and the same view.
     * @param model model of attributes
     * @return view name
     * @since 1.0
     * @see ApplicationSecurityConfig
     */
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

}
