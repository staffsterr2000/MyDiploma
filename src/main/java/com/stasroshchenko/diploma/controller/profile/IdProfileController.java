package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/id")
@AllArgsConstructor
public class IdProfileController {

    private final ApplicationUserService applicationUserService;

    @GetMapping("/{username}")
    public String getIdProfileView(
            @PathVariable("username") String username,
            Model model
    ) {

        ApplicationUser userByUsername =
                applicationUserService.getByUsername(username);
        model.addAttribute("requiredUser", userByUsername);

        return "id_profile";
    }

}
